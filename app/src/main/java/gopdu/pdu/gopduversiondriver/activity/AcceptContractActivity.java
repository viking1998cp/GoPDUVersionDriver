package gopdu.pdu.gopduversiondriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityAcceptContractBinding;
import gopdu.pdu.gopduversiondriver.network.ImageRespon;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.presenter.PresenterAcceptContract;
import gopdu.pdu.gopduversiondriver.view.ViewAcceptContractListener;
import gopdu.pdu.gopduversiondriver.viewmodel.UploadAccountViewModel;
import gopdu.pdu.gopduversiondriver.viewmodel.UploadImageViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AcceptContractActivity extends BaseActivity implements ViewAcceptContractListener  {
    
    private ActivityAcceptContractBinding binding;
    private UploadImageViewModel uploadImageModel;
    private PresenterAcceptContract presenter;
    private Driver driver;
    private ArrayList<ImageAlbum> imageURL;
    private UploadAccountViewModel uploadAccountModel;
    private ProgressDialog progress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_accept_contract;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accept_contract);
        init();
        setUpOnClick();
        ActionToolbar();
    }

    private void init() {
        imageURL = new ArrayList<>();
        if(getIntent().getSerializableExtra(getString(R.string.objectDriver)) != null){
            driver =  (Driver) getIntent().getSerializableExtra(getString(R.string.objectDriver));
        }
        uploadImageModel = ViewModelProviders.of(this).get(UploadImageViewModel.class);
        uploadAccountModel = ViewModelProviders.of(this).get(UploadAccountViewModel.class);
        progress = new ProgressDialog(AcceptContractActivity.this);
        progress.setMessage(getString(R.string.waitingProcess));
        progress.setCanceledOnTouchOutside(false);
        presenter = new PresenterAcceptContract(this);
    }

    private void setUpOnClick() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                uploadToServer(driver.getImvLicensedriverFront(), driver.getImvLicensedriverBackside(),
                        driver.getImvIdentitycardFront(), driver.getImvIdentitycardBackside(),
                        driver.getImvMotorcyclepapersFront(), driver.getImvMotorcyclepapersBackside(),
                        driver.getImvDriverface());
            }
        });
    }
    private void uploadToServer(String licensesPathFront, String licensesPathBackside,
                                String identityCardFront, final String identityCardBackside,
                                String motocyclepaperFront, String motocyclepaperBackside,
                                String driverFace) {
        progress.show();
        // Map is used to multipart the file using okhttp3.RequestBody
        File licensesPathFrontFile = new File(licensesPathFront);
        File licensesPathBacksideFile = new File(licensesPathBackside);
        File identityCardFrontFile = new File(identityCardFront);
        File identityCardBacksideFile = new File(identityCardBackside);

        File motocyclepaperFrontFile = new File(motocyclepaperFront);
        File motocyclepaperBacksideFile = new File(motocyclepaperBackside);
        File driverFaceFile = new File(driverFace);

        // Parsing any Media type file
        RequestBody requestLicenseFront = RequestBody.create(MediaType.parse("*/*"), licensesPathFrontFile);
        RequestBody requestLicenseBackside = RequestBody.create(MediaType.parse("*/*"), licensesPathBacksideFile);

        RequestBody requestidentityFront = RequestBody.create(MediaType.parse("*/*"), identityCardFrontFile);
        RequestBody requestidentityBackside = RequestBody.create(MediaType.parse("*/*"), identityCardBacksideFile);

        RequestBody requestMotocyclepaperFront = RequestBody.create(MediaType.parse("*/*"), motocyclepaperFrontFile);
        RequestBody requestMotocyclepaperBackside = RequestBody.create(MediaType.parse("*/*"), motocyclepaperBacksideFile);

        RequestBody requestDriverFace = RequestBody.create(MediaType.parse("*/*"), driverFace);

        MultipartBody.Part fileToUploadLicenseFront = MultipartBody.Part.createFormData(getString(R.string.licenseDriverFront), licensesPathFrontFile.getName(), requestLicenseFront);
        MultipartBody.Part fileToUploadLicenseBackside = MultipartBody.Part.createFormData(getString(R.string.licenseDriverBackSide), licensesPathBacksideFile.getName(), requestLicenseBackside);

        MultipartBody.Part fileToUploaPartIdentityCardFront = MultipartBody.Part.createFormData(getString(R.string.identityFront), identityCardFrontFile.getName(), requestidentityFront);
        MultipartBody.Part fileToUploaPartIdentityCardBackside = MultipartBody.Part.createFormData(getString(R.string.identityBackSide),  identityCardBacksideFile.getName(), requestidentityBackside);

        MultipartBody.Part fileToUploadMotocyclepaperFront = MultipartBody.Part.createFormData(getString(R.string.motorcyclepapersFront), motocyclepaperFrontFile.getName(), requestMotocyclepaperFront);
        MultipartBody.Part fileToUploadMotocyclepaperBackside = MultipartBody.Part.createFormData(getString(R.string.motorcyclepapersBackSide), motocyclepaperBacksideFile.getName(), requestMotocyclepaperBackside);

        MultipartBody.Part fileToDriverFace = MultipartBody.Part.createFormData(getString(R.string.driverFace), driverFaceFile.getName(), requestDriverFace);

        uploadImageModel.uploadImage(fileToUploadLicenseFront,
                fileToUploadLicenseBackside,
                fileToUploaPartIdentityCardFront,
                fileToUploaPartIdentityCardBackside,
                fileToUploadMotocyclepaperFront,
                fileToUploadMotocyclepaperBackside,
                fileToDriverFace).observe(this, new Observer<ImageRespon>() {
            @Override
            public void onChanged(ImageRespon imageRespon) {
                ArrayList<ImageAlbum> imagesUrl = imageRespon.getData();
                presenter.checkImageUrl(imagesUrl);
            }
        });



    }


    @Override
    public void notNullUrlImage(ArrayList<ImageAlbum> imageUrl) {
        imageURL.addAll(imageUrl);
        presenter.getParamUpload(driver, imageUrl);
    }

    @Override
    public void nullUrlImage() {
        Common.ShowToastShort(getString(R.string.checkConnect));
    }

    @Override
    public void getParamUpload(Map<String, String> param) {
        Log.d("BBB", "getParamUpload: "+param.get(getString(R.string.paramID)));
        uploadAccountModel.uploadAccount(param).observe(this, new Observer<ServerResponse>() {
            @Override
            public void onChanged(ServerResponse serverResponse) {
                progress.dismiss();
                presenter.checkUploadAccount(serverResponse.getSuccess());
            }
        });
    }

    @Override
    public void registerSuccess() {
        Common.ShowToastShort(getString(R.string.registerSuccess));
        Intent intent = new Intent(AcceptContractActivity.this, RegisterResultActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(getString(R.string.resultRegister),getString(R.string.registerSuccess));
        startActivity(intent);
        finish();
    }

    @Override
    public void registerFaild() {
        Common.ShowToastShort(getString(R.string.registerFaild));
        Intent intent = new Intent(AcceptContractActivity.this, RegisterResultActivity.class);
        intent.putExtra(getString(R.string.resultRegister),getString(R.string.registerFaild));
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
