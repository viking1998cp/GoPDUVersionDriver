package gopdu.pdu.gopduversiondriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityDriverFaceBinding;
import gopdu.pdu.gopduversiondriver.network.ImageRespon;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.presenter.PresenterDiverFaceImage;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import gopdu.pdu.gopduversiondriver.view.ViewDriverFaceImageListener;


public class DriverFaceActivity extends BaseActivity implements ViewDriverFaceImageListener {

    private ActivityDriverFaceBinding binding;
    private Driver driver;
    private ProgressDialog progress;
    private PresenterDiverFaceImage presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_face;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DriverFaceActivity.this, R.layout.activity_driver_face);

        init();
        setUpOnClick();
        ActionToolbar();
    }

    private void init() {
        driver = new Driver();
        if(getIntent().getSerializableExtra(getString(R.string.objectDriver)) != null){
            driver =  (Driver) getIntent().getSerializableExtra(getString(R.string.objectDriver));
        }
        progress = new ProgressDialog(DriverFaceActivity.this);
        progress.setMessage(getString(R.string.waitingProcess));
        progress.setCanceledOnTouchOutside(false);
        presenter = new PresenterDiverFaceImage(this);
    }

    private void setUpOnClick() {
        binding.imvFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverFaceActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.driverFace));
                startActivityForResult(intent, Common.RQUEST_CODE_DRIVER_FACE);

            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.checkNullImage(driver);


            }
        });
    }

    private ImageAlbum imageAlbum;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageAlbum = (ImageAlbum) data.getSerializableExtra(getString(R.string.imv));
            if (requestCode == Common.RQUEST_CODE_DRIVER_FACE && resultCode == Common.RQUEST_CODE_DRIVER_FACE) {
                presenter.getData(requestCode,resultCode);

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(DriverFaceActivity.this);
    }


    @Override
    public void onDriverFaceImage() {
        driver.setImvDriverface(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imvFront);
    }

    @Override
    public void nullImage() {
        Common.ShowToastShort(getString(R.string.errorDriverFace));
    }

    @Override
    public void notNullImage() {
        Intent intent = new Intent(DriverFaceActivity.this, AcceptContractActivity.class);
        intent.putExtra(getString(R.string.objectDriver), driver);
        startActivity(intent);
        Animatoo.animateSlideLeft(DriverFaceActivity.this);
    }
    private void ActionToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
