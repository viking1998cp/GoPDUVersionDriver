package gopdu.pdu.gopduversiondriver.activity;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.HashMap;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityHistoryDetailBinding;
import gopdu.pdu.gopduversiondriver.network.HistoryDetailRespon;
import gopdu.pdu.gopduversiondriver.object.HistoryDetail;
import gopdu.pdu.gopduversiondriver.presenter.PresenterHistoryDetailActivity;
import gopdu.pdu.gopduversiondriver.view.ViewHistoryDetailActivityListener;
import gopdu.pdu.gopduversiondriver.viewmodel.GetHistoryDetailViewModel;

public class HistoryDetailActivityActivity extends FragmentActivity implements OnMapReadyCallback , ViewHistoryDetailActivityListener {

    private GoogleMap mMap;
    private GetHistoryDetailViewModel getHistoryDetailModel;
    private PresenterHistoryDetailActivity presenter;
    private ProgressDialog progressDialog;
    private ActivityHistoryDetailBinding binding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
        getData();
    }

    private void init() {
        userId = FirebaseAuth.getInstance().getUid();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.waitingProcess));
        progressDialog.show();
        presenter = new PresenterHistoryDetailActivity(this);
        getHistoryDetailModel = ViewModelProviders.of(this).get(GetHistoryDetailViewModel.class);
    }

    private void getData() {
        if(getIntent().getStringExtra(getString(R.string.paramID)) != null){
            String idHistory = getIntent().getStringExtra(getString(R.string.paramID));
            HashMap param = new HashMap();
            param.put(getString(R.string.paramID), idHistory);
            param.put(getString(R.string.paramUserId), userId);
            getHistoryDetailModel.getHistoryResponseLiveData(param).observe(this, new Observer<HistoryDetailRespon>() {
                @Override
                public void onChanged(HistoryDetailRespon historyDetailRespon) {

                    presenter.reciverDataHistoryDetail(historyDetailRespon);
                }
            });
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    //Zoom map
    private void zoomTarget(double lat, double lng) {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(12).build();
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //addMyMarker
    private Marker addMyMarker(double lat, double lng, int idIcon, String title) {
        return mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromResource(idIcon)).title(title));
    }

    @Override
    public void getDataHistorySuccess(HistoryDetail data) {
        setUpView(data);
    }

    private void setUpView(HistoryDetail data) {
        progressDialog.dismiss();
        Log.d("BBB", "setUpView: "+data.getHistory());
        binding.tvIdHistory.setText(getString(R.string.booking, data.getHistory().getId()));
        LatLng pickUpLng = new LatLng(data.getHistory().getPickupLat(), data.getHistory().getPickupLogt());
        LatLng destinationLng = new LatLng(data.getHistory().getDestinationLat(), data.getHistory().getDestinationLogt());
        addMyMarker(pickUpLng.latitude, pickUpLng.longitude, R.mipmap.ic_driver_handle,getString(R.string.pickupShowScreen));
        addMyMarker(destinationLng.latitude, destinationLng.longitude, R.mipmap.ic_destination,getString(R.string.destinationShowScreen));
        zoomTarget(pickUpLng.latitude, pickUpLng.longitude);
        binding.tvPickUpName.setText(data.getHistory().getPickupName());
        binding.tvDestinationName.setText(data.getHistory().getDestinationName());
        binding.tvDistance.setText(getString(R.string.distance, Common.getDistance(pickUpLng, destinationLng)));
        binding.tvPrice.setText(getString(R.string.price, Common.formatVNĐ(data.getHistory().getPrice())));
        binding.tvCustomerName.setText(data.getCustomer().getName());
        binding.tvCustomerPhone.setText(data.getCustomer().getNumberphone());

        if(data.getHistory().getRating() >=0){
            binding.ratting.setRating(data.getHistory().getRating());
            binding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.background_button_no_boder_gray));
            binding.btnSubmit.setClickable(false);
        }

    }

    @Override
    public void getDataHistoryFaild() {
        progressDialog.dismiss();
        Common.ShowToastShort(getString(R.string.checkConnect));

    }
}
