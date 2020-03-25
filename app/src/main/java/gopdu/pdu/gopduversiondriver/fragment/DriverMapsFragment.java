package gopdu.pdu.gopduversiondriver.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tecorb.hrmovecarmarkeranimation.AnimationClass.HRMarkerAnimation;
import com.tecorb.hrmovecarmarkeranimation.CallBacks.UpdateLocationCallBack;

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

public class DriverMapsFragment extends Fragment implements ViewDriverMapFragmentListener, OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, IOnBackPressed {

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
    private String statusTrip;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ProgressDialog progressDialog;
    private int markerCount = 0;
    private Location oldLocation;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driver_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        init();
        setOnClick();
        getTravel();
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
        binding.swWorking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                presenter.reciverWorking(isChecked);
            }
        });
    }


    public void addMarker(GoogleMap googleMap, double lat, double lon) {
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
                    addMarker(mMap, mLastLocation.getLatitude(), mLastLocation.getLongitude());
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

                    DatabaseReference refAvailble = FirebaseDatabase.getInstance().getReference("driversAvailable");
                    DatabaseReference refWorKing = FirebaseDatabase.getInstance().getReference("driverWorking");
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

    private void getTravel() {

        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("User").child("Driver").child(driverId).child("customerRequest").child("customerRideId");
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && customerId.equals("")) {
                    if (binding.swWorking.isChecked()) {

                        Log.d("BBB", "onDataChange: table on");
//                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(view.getContext().VIBRATOR_SERVICE);
//                        until.ring(view.getContext(), vibrator,5000 );
                    }

                    customerId = dataSnapshot.getValue().toString();
                    getLocationCustomer();
                    getInfomationTravel();
                    binding.contentTravel.cdlInfomationTravel.setVisibility(View.VISIBLE);

//                    laythongtinkhachhang();


                    Log.d("AAA", "onDataChange: " + customerId);
                } else {
                    binding.contentTravel.cdlInfomationTravel.setVisibility(View.GONE);
                    statusTrip = "";
                    customerId = "";


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private LatLng destinaLatLng;
    private Marker destinaMaker;
    private void getInfomationTravel() {
        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("BBB", "getAssignedCustomer: "+driverId);
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("User").child("Driver").child(driverId).child("customerRequest");
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    java.util.Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Double destinationLat = 0.0;
                    Double destinationlongt = 0.0;
                    if(map.get("destinationLat")!= null){
                        destinationLat = Double.valueOf(map.get("destinationLat").toString());
                    }
                    if(map.get("destinationLongt")!= null){
                        destinationlongt = Double.valueOf(map.get("destinationLongt").toString());
                    }
                    destinaLatLng = new LatLng(destinationLat,destinationlongt);
                    presenter.reciverDestinationName(destinaLatLng);

                    if(map.get("price") != null){
                        price = Integer.parseInt(map.get("price").toString());
                        binding.contentTravel.tvPrice.setText(getString(R.string.price, Common.formatVNĐ(price)));
                    }

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
        FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("l").addListenerForSingleValueEvent(new ValueEventListener() {
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


    //addMarker
    private Marker addMarker(double lat, double lng, int idIcon, String title) {
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
        Log.d("BBB", "connectWorking: "+accountDriver.getGender());
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

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
}
