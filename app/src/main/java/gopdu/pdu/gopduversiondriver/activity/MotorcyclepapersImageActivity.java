package gopdu.pdu.gopduversiondriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.koushikdutta.ion.Ion;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityMotorcyclepapersImageBinding;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.presenter.PresenterMotocyclePaperImage;
import gopdu.pdu.gopduversiondriver.view.ViewMotorcyclepaperImageListener;

public class MotorcyclepapersImageActivity extends BaseActivity implements ViewMotorcyclepaperImageListener {

    private ActivityMotorcyclepapersImageBinding binding;
    private Driver driver;
    private PresenterMotocyclePaperImage presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_motorcyclepapers_image_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MotorcyclepapersImageActivity.this, R.layout.activity_motorcyclepapers_image_);

        init();
        setUpOnClick();
        ActionToolbar();
    }

    private void init() {
        driver = new Driver();
        if (getIntent().getSerializableExtra(getString(R.string.objectDriver)) != null) {
            driver = (Driver) getIntent().getSerializableExtra(getString(R.string.objectDriver));
        }
        presenter = new PresenterMotocyclePaperImage(this);
    }

    private void setUpOnClick() {
        binding.imv.imvFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MotorcyclepapersImageActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.motorcyclepapersFront));
                startActivityForResult(intent, Common.RQUEST_CODE_MOTORCYCLEPAPER_FRONT);


            }
        });
        binding.imv.imvBackSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MotorcyclepapersImageActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.motorcyclepapersBackSide));
                startActivityForResult(intent, Common.RQUEST_CODE_MOTORCYCLEPAPER_BACKSIDE);
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
            presenter.getData(requestCode, resultCode);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(MotorcyclepapersImageActivity.this);
    }

    @Override
    public void onMotorcyclepaperImageFront() {
        driver.setImvMotorcyclepapersFront(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imv.imvFront);
    }

    @Override
    public void onMotorcyclepaperImageBackside() {
        driver.setImvMotorcyclepapersBackside(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imv.imvBackSide);
    }

    @Override
    public void nullImage() {
        Common.ShowToastShort(getString(R.string.errorMotorcycleImage));
    }

    @Override
    public void notNullImage() {
        Intent intent = new Intent(MotorcyclepapersImageActivity.this, DriverFaceActivity.class);
        intent.putExtra(getString(R.string.objectDriver), driver);
        startActivity(intent);
        Animatoo.animateSlideLeft(MotorcyclepapersImageActivity.this);
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
