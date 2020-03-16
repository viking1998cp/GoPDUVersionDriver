package gopdu.pdu.gopduversiondriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.koushikdutta.ion.Ion;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityLicenseImageDriverBinding;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.presenter.PresenterLicenseDriverImage;
import gopdu.pdu.gopduversiondriver.view.VIewLicenseDriverImageListener;

public class LicenseDriverImageActivity extends BaseActivity implements VIewLicenseDriverImageListener {

    private ActivityLicenseImageDriverBinding binding;
    private Driver driver;
    private PresenterLicenseDriverImage presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_license_image_driver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(LicenseDriverImageActivity.this, R.layout.activity_license_image_driver);
        init();
        setUpOnClick();
        ActionToolbar();
    }

    private void init() {
        driver = new Driver();
        if(getIntent().getSerializableExtra(getString(R.string.objectDriver)) != null){
            driver = (Driver) getIntent().getSerializableExtra(getString(R.string.objectDriver));
        }
        presenter = new PresenterLicenseDriverImage(this);
    }

    private void setUpOnClick() {
        binding.imv.imvFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LicenseDriverImageActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.licenseDriverFront));
                startActivityForResult(intent, Common.RQUEST_CODE_LICENSE_FRONT);

            }
        });
        binding.imv.imvBackSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LicenseDriverImageActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.licenseDriverBackSide));
                startActivityForResult(intent, Common.RQUEST_CODE_LICENSE_BACK);
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkNulImage(driver);
            }
        });

    }

    private ImageAlbum imageAlbum;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            imageAlbum = (ImageAlbum) data.getSerializableExtra(getString(R.string.imv));
            presenter.getDataImage(requestCode, resultCode);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(LicenseDriverImageActivity.this);
    }

    @Override
    public void onLicenseDriverImageFront() {
        driver.setImvLicensedriverFront(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imv.imvFront);
    }

    @Override
    public void onLicenseDriverImageBackside() {
        driver.setImvLicensedriverBackside(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imv.imvBackSide);
    }

    @Override
    public void nullImage() {
        Common.ShowToastShort(getString(R.string.errorLicenseDriver));
    }

    @Override
    public void notNullImage() {
        Intent intent = new Intent(LicenseDriverImageActivity.this, IdentitycardImageActivity.class);
        intent.putExtra(getString(R.string.objectDriver), driver);
        startActivity(intent);
        Animatoo.animateSlideLeft(LicenseDriverImageActivity.this);
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
