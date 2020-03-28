package gopdu.pdu.gopduversiondriver.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecorb.hrmovecarmarkeranimation.AnimationClass.HRMarkerAnimation;
import com.tecorb.hrmovecarmarkeranimation.CallBacks.UpdateLocationCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.IOnBackPressed;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.FragmentDriverMapsBinding;
import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.presenter.PresenterDriverMapFragment;
import gopdu.pdu.gopduversiondriver.view.ViewDriverMapFragmentListener;
import gopdu.pdu.gopduversiondriver.viewmodel.TakenAccountInfomationViewModel;

public class DriverMapsFragment extends Fragment implements ViewDriverMapFragmentListener, OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, IOnBackPressed, RoutingListener {

    private FragmentDriverMapsBinding binding;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    //request old location
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    //Presenter
    private PresenterDriverMapFragment presenter;

    private String customerId = "";
    private String userId;
    private boolean requestBoolena = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ProgressDialog progressDialog;
    private int markerCount = 0;
    private Location oldLocation;

    private String statusTrip="";
    private DatabaseReference tripRef;

    private Driver accountDriver;

    //service infomation account
    private TakenAccountInfomationViewModel accountInfomation;
    private DatabaseReference mdDataPushWorking;

    //travel
    private int price;
    private String namePickUpName;
    private String namePickUpNameDetail;
    private String destinationName;
    private String destinationNameDetail;
    private float distance=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driver_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        init();
        setOnClick();
        getJob();
        return binding.getRoot();
    }

    private void init() {


        //Presenter setup
        presenter = new PresenterDriverMapFragment(this);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.waitingProcess));
        progressDialog.setCancelable(false);
        progressDialog.show();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(binding.getRoot().getContext());
        //Get user id
        userId = FirebaseAuth.getInstance().getUid();

        //setup service
        accountInfomation = ViewModelProviders.of(this).get(TakenAccountInfomationViewModel.class);
        accountDriver = new Driver();


        //infomation account
        HashMap<String, String> param = new HashMap<>();
        param.put(getString(R.string.paramID), userId);
        accountInfomation.TakenInfomationAccount(param).observe(this, new Observer<AccountResponse>() {
            @Override
            public void onChanged(AccountResponse accountResponse) {
                presenter.reciverInfomationAccount(accountResponse);
            }
        });

        //Google api set up
        buildGoogleAPiClient();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void setOnClick() {

        //On off working
        binding.swWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                presenter.reciverWorking(isChecked);
            }
        });

        //
        binding.contentTravel.btnStartPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickUpCustomer();
            }
        });
    }



    public void addMyMarker(GoogleMap googleMap, double lat, double lon) {
        try {

            if (markerCount == 1) {
                if (oldLocation != null) {
                    new HRMarkerAnimation(googleMap, 1000, new UpdateLocationCallBack() {
                        @Override
                        public void onUpdatedLocation(Location updatedLocation) {
                            oldLocation = updatedLocation;
                        }
                    }).animateMarker(mLastLocation, oldLocation, myMarker);
                } else {
                    oldLocation = mLastLocation;
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18f));
            } else if (markerCount == 0) {
                if (myMarker != null) {
                    myMarker.remove();
                }
                mMap = googleMap;

                LatLng latLng = new LatLng(lat, lon);

                myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_foreground)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));

                /*################### Set Marker Count to 1 after first marker is created ###################*/

                markerCount = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null && mLastLocation.getLongitude() != 0.0 && mLastLocation.getLongitude() != 0.0) {

                if (mMap != null) {
                    addMyMarker(mMap, mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private HRMarkerAnimation myLocation;
    private Marker myMarker;
    final LocationCallback mlocationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getActivity() != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    if(customerId.equals("")){
//                        rideDistance += mLastLocation.distanceTo(location)/1000;
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                        if(!polylines.isEmpty()){
//                            erasePolyLines();
//                        }
//                    }

                    mLastLocation = location;
                    displayLocation();
                    if(statusTrip.equals(getString(R.string.tripPickUp))){
                        chiduongtoikhachhang(pickupLatLng);
                    }
                    DatabaseReference refAvailble = FirebaseDatabase.getInstance().getReference(getString(R.string.paramDriverAvailable));
                    DatabaseReference refWorKing = FirebaseDatabase.getInstance().getReference(getString(R.string.paramDriverWorking));
                    GeoFire geoAvailble = new GeoFire(refAvailble);
                    GeoFire geoWorking = new GeoFire(refWorKing);
                    switch (customerId) {
                        case "":
                            geoWorking.removeLocation(userId);
                            geoAvailble.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;
                        default:
                            geoAvailble.removeLocation(userId);
                            geoWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;
                    }

                }
            }
        }
    };

    private void getJob() {

        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.paramUser)).child(getString(R.string.paramDriver)).child(driverId).child(getString(R.string.paramCustomerRequest)).child(getString(R.string.paramCustomerId));
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("BBB", "onDataChange: "+dataSnapshot);
                    if(binding.swWorking.isChecked()){
                        if (dataSnapshot.exists() && customerId.equals("")) {
//                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(view.getContext().VIBRATOR_SERVICE);
//                        until.ring(view.getContext(), vibrator,5000 );
                            customerId = dataSnapshot.getValue().toString();
                            showInfomationJob();


                        } else {
                            binding.contentTravel.cdlInfomationTravel.setVisibility(View.GONE);
                            statusTrip = "";
                            customerId = "";

                        }
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showInfomationJob() {
        getLocationCustomer();
        getInfomationTravel();
        binding.contentTravel.cdlInfomationTravel.setVisibility(View.VISIBLE);
        setUpTimerJob();
    }

    private void showPickUpCustomer() {

        timerListener.cancel();
        binding.contentTravel.cdlInfomationTravel.setVisibility(View.GONE);
        binding.contentPickup.cdlPickUpCustomer.setVisibility(View.VISIBLE);
        binding.contentPickup.tvNamePickupDetail.setText(namePickUpNameDetail);
        binding.contentPickup.tvPrice.setText(getString(R.string.price, Common.formatVNĐ(price)));
        //Trip listener
        if(tripRef == null){
            tripRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.paramUser)).child(getString(R.string.paramCustomer)).child(customerId).child(getString(R.string.driverRequest)).child(getString(R.string.statusTrip));
        }
        statusTrip = getString(R.string.tripPickUp);
        tripRef.setValue(statusTrip);
        chiduongtoikhachhang(pickupLatLng);

    }


    // Setup time accept job ( 20s )
    private int timerWaitingJob ;
    private CountDownTimer timerListener;
    private void setUpTimerJob() {
        timerWaitingJob = 20;
        if(timerListener ==null){
            timerListener = new CountDownTimer(20000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timerWaitingJob--;
                    binding.contentTravel.tvTimerJob.setText(timerWaitingJob+"");
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    endRide();
                }

            };
        }
        timerListener.start();

    }

    // end ride
    private void endRide() {


        binding.contentTravel.cdlInfomationTravel.setVisibility(View.GONE);
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.paramUser)).child(getString(R.string.paramDriver)).child(userId).child(getString(R.string.paramCustomerRequest));
        driverRef.removeValue();

        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.paramUser)).child(getString(R.string.paramCustomer)).child(customerId).child(getString(R.string.driverRequest));
        customerRef.removeValue();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.paramCustomerRequest));
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(customerId);
        customerId="";

        if(pickupMarker != null){
            pickupMarker.remove();
        }

        binding.contentTravel.tvDistance.setText("");
        binding.contentTravel.tvTimerJob .setText(getString(R.string.timerWaittingJob));
        binding.contentTravel.tvNamePickup.setText("");
        binding.contentTravel.tvNamePickupDetail.setText("");
        binding.contentTravel.tvNameDestination.setText("");
        binding.contentTravel.tvNameDestinationDetail.setText("");
        binding.contentTravel.tvPrice.setText("");

        customerId ="";
        if(pickupMarker != null){
            pickupMarker.remove();
        }
        if(customerPickupLocationRefListener != null){
            customerPickupLocationRef.removeEventListener(customerPickupLocationRefListener);
        }
        statusTrip = "";
    }

    private LatLng destinaLatLng;
    private Marker destinaMaker;
    private void getInfomationTravel() {
        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.paramUser)).child(getString(R.string.paramDriver)).child(driverId).child(getString(R.string.paramCustomerRequest));
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    java.util.Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Double destinationLat = 0.0;
                    Double destinationlongt = 0.0;
                    if(map.get(getString(R.string.paramDestinationLat))!= null){
                        destinationLat = Double.valueOf(map.get(getString(R.string.paramDestinationLat)).toString());
                    }
                    if(map.get(getString(R.string.paramDestinationLogt))!= null){
                        destinationlongt = Double.valueOf(map.get(getString(R.string.paramDestinationLogt)).toString());
                    }
                    destinaLatLng = new LatLng(destinationLat,destinationlongt);

                    if(map.get(getString(R.string.paramPrice)) != null){
                        price = Integer.parseInt(map.get(getString(R.string.paramPrice)).toString());
                        binding.contentTravel.tvPrice.setText(getString(R.string.price, Common.formatVNĐ(price)));
                    }

                    if(map.get(getString(R.string.paramDistance)) != null){
                        distance = Float.parseFloat(map.get(getString(R.string.paramDistance)).toString());
                    }
                    if(map.get(getString(R.string.paramDistance)) != null){
                        distance = Float.parseFloat(map.get(getString(R.string.paramDistance)).toString());
                    }

                    presenter.reciverDestinationName(destinaLatLng);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Marker pickupMarker;
    private LatLng pickupLatLng;
    private DatabaseReference customerPickupLocationRef;
    private  ValueEventListener customerPickupLocationRefListener;
    private void getLocationCustomer() {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.paramCustomerRequest)).child(customerId).child(getString(R.string.paramL)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && !customerId.equals("")){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLongi = 0;
                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLongi = Double.parseDouble(map.get(1).toString());
                    }
                    pickupLatLng = new LatLng(locationLat,locationLongi);
                    presenter.reciverPickUpname(pickupLatLng);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();

    }

    //Zoom map
    private void zoomTarget(double lat, double lng) {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(15).build();
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //addMyMarker
    private Marker addMyMarker(double lat, double lng, int idIcon, String title) {
        return mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .fromResource(idIcon)).title(title));
    }


    // Create google API client
    protected synchronized void buildGoogleAPiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setMyLocationEnabled(false);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void takenInfomationAccount(Driver data) {
        accountDriver = data;
        mdDataPushWorking = FirebaseDatabase.getInstance().getReference().child("User").child("Driver").child(userId);
        progressDialog.dismiss();

    }

    @Override
    public void connectWorking() {
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mlocationCallBack, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        HashMap userInfo = new HashMap();
        userInfo.put(getString(R.string.paramName),accountDriver.getName());
        userInfo.put(getString(R.string.paramNumberPhone),accountDriver.getNumberphone());
        userInfo.put(getString(R.string.paramGender),accountDriver.getGender());
        userInfo.put(getString(R.string.paramImv_Driverface),accountDriver.getImvDriverface());
        userInfo.put(getString(R.string.paramBirthDate), accountDriver.getBirthdate());
        userInfo.put(getString(R.string.paramLicenseplate), accountDriver.getLicenseplates());

        mdDataPushWorking.updateChildren(userInfo);
    }

    @Override
    public void dissconnectWorking() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mlocationCallBack);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.paramDriverAvailable));

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
        mMap.setMyLocationEnabled(false);
        mdDataPushWorking.removeValue();
    }

    @Override
    public void getPickUpName(Address pickup) {
        String nameDetail = pickup.getAddressLine(0);
        String[] names = nameDetail.split(",");


        namePickUpName =  names[0] + "," + names[1];
        namePickUpNameDetail = nameDetail;

        binding.contentTravel.tvNamePickup.setText(namePickUpName);
        binding.contentTravel.tvNamePickupDetail.setText(namePickUpNameDetail);

    }

    @Override
    public void getDestinationName(Address destination) {
        String nameDetail = destination.getAddressLine(0);
        String[] names = nameDetail.split(",");

        destinationName = names[0] + "," + names[1];;
        destinationNameDetail = nameDetail;

        binding.contentTravel.tvNameDestination.setText(destinationName);
        binding.contentTravel.tvNameDestinationDetail.setText(destinationNameDetail);

    }

    //Driection
    private void chiduongtoikhachhang(LatLng pickupLatLng) {
        if(pickupLatLng != null  && mLastLocation != null){
            Routing routing = new Routing.Builder()
                    .key(getString(R.string.google_api_key))
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()), pickupLatLng)
                    .build();
            routing.execute();
        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Common.ShowToastShort(e.getMessage());
            Log.d("BBB", "onRoutingFailure: "+e.getMessage().toString());
        }else {
            Common.ShowToastShort(getString(R.string.checkConnect));
        }
    }

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polyline.setColor(Color.GREEN);
            polylines.add(polyline);

//            Toast.makeText(view.getContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
            route.get(i).getDistanceValue();
        }
    }
    public void erasePolyLines(){
        for (Polyline line: polylines){
            line.remove();
        }
        polylines.clear();
    }

    @Override
    public void onRoutingCancelled() {

    }
}
