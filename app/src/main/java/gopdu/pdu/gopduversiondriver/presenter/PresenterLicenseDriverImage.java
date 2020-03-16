package gopdu.pdu.gopduversiondriver.presenter;

import android.content.Intent;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.modelresponse.LicenseDriverImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.view.VIewLicenseDriverImageListener;
import gopdu.pdu.gopduversiondriver.viewmodel.LicenseDriverImageViewModel;

public class PresenterLicenseDriverImage implements LicenseDriverImageResponse {
    private LicenseDriverImageViewModel licenseViewModel;
    private VIewLicenseDriverImageListener callback;

    public PresenterLicenseDriverImage(VIewLicenseDriverImageListener callback) {
        this.licenseViewModel = new LicenseDriverImageViewModel(this);
        this.callback = callback;
    }

    public void getDataImage( int requestCode, int resultCode) {
        licenseViewModel.getData(requestCode, resultCode);
    }

    public void checkNulImage(Driver driver){
        licenseViewModel.checkNullImage(driver);
    }

    @Override
    public void onLicenseDriverImageFront() {
        callback.onLicenseDriverImageFront();
    }

    @Override
    public void onLicenseDriverImageBackside() {
        callback.onLicenseDriverImageBackside();
    }

    @Override
    public void nullImage() {
        callback.nullImage();
    }

    @Override
    public void notNullImage() {
        callback.notNullImage();
    }
}
