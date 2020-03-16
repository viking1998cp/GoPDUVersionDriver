package gopdu.pdu.gopduversiondriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.koushikdutta.ion.Ion;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityIdentitycardImageBinding;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.presenter.PresenterIdentityCardImage;
import gopdu.pdu.gopduversiondriver.view.ViewIdentityCardImageListener;

public class IdentitycardImageActivity extends BaseActivity implements ViewIdentityCardImageListener {

    private ActivityIdentitycardImageBinding binding;
    private Driver driver;
    private PresenterIdentityCardImage presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_identitycard_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(IdentitycardImageActivity.this, R.layout.activity_identitycard_image);

        init();
        setUpOnClick();
        ActionToolbar();
    }

    private void init() {
        driver =new Driver();
        if(getIntent().getSerializableExtra(getString(R.string.objectDriver)) != null){
            driver =  (Driver) getIntent().getSerializableExtra(getString(R.string.objectDriver));
        }
        presenter = new PresenterIdentityCardImage(this);

    }

    private void setUpOnClick() {
        binding.imv.imvFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentitycardImageActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.identityFront));
                startActivityForResult(intent, Common.RQUEST_CODE_IDENTITY_CARD_FRONT);

            }
        });
        binding.imv.imvBackSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentitycardImageActivity.this, CameraActivity.class);
                intent.putExtra(getString(R.string.request_imv), getString(R.string.identityBackSide));
                startActivityForResult(intent, Common.RQUEST_CODE_IDENTITY_CARD_BACKSIDE);
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkNulImage(driver);
            }
        });


    }

    private  ImageAlbum imageAlbum;
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
        Animatoo.animateSlideRight(IdentitycardImageActivity.this);
    }

    @Override
    public void onIdentityCardImageFront() {
        driver.setImvIdentitycardFront(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imv.imvFront);
    }

    @Override
    public void onIdentityCardImageBackside() {
        driver.setImvIdentitycardBackside(imageAlbum.getPath());
        Ion.with(getApplication()).load(imageAlbum.getPath()).intoImageView(binding.imv.imvBackSide);
    }

    @Override
    public void nullImage() {
        Common.ShowToastShort(getString(R.string.errorIdentityCard));
    }

    @Override
    public void notNullImage() {
        Intent intent = new Intent(IdentitycardImageActivity.this, MotorcyclepapersImageActivity.class);
        intent.putExtra(getString(R.string.objectDriver), driver);
        startActivity(intent);
        Animatoo.animateSlideLeft(IdentitycardImageActivity.this);
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
