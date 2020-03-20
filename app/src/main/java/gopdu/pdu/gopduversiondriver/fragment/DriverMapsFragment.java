package gopdu.pdu.gopduversiondriver.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.accounts.Account;
import android.app.ProgressDialog;
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
    TakenAccountInfomationViewModel accountInfomation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driver_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        init();
        setOnClick();
        laychuyendi();
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
                if (isChecked) {
                    connectDriver();
                } else {
                    disconnectDriver();
                }

            }
        });
    }

    private void connectDriver() {

        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mlocationCallBack, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


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

    private void disconnectDriver() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mlocationCallBack);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
        mMap.setMyLocationEnabled(false);

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

    private void laychuyendi() {

        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("User").child("Driver").child(driverId).child("customerRequest").child("customerRideId");
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (binding.swWorking.isChecked()) {
//                        coordinatorCustomerInfo.setVisibility(View.VISIBLE);
//                        Log.d("BBB", "onDataChange: table on");
//                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(view.getContext().VIBRATOR_SERVICE);
//                        until.ring(view.getContext(), vibrator,5000 );
                    }
//                    bottomSheet =  view.findViewById(R.id.bottom_sheet);
//                    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
//                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    status =1;
                    customerId = dataSnapshot.getValue().toString();
//                    layvitrikhachhang();
//                    laynoidenkhachhang();
//                    laythongtinkhachhang();


                    Log.d("AAA", "onDataChange: " + customerId);
                } else {
//                    coordinatorCustomerInfo.setVisibility(View.GONE);
//                    statusTrip = "";
                    customerId = "";
//                    if(pickupMarker != null){
//                        pickupMarker.remove();
//                    }


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
        Log.d("BBB", "takenInfomationAccount: " + accountDriver.getImvMotorcyclepapersBackside());
        progressDialog.dismiss();

    }
}
