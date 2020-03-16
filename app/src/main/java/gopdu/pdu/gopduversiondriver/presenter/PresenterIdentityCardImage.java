package gopdu.pdu.gopduversiondriver.presenter;

import gopdu.pdu.gopduversiondriver.modelresponse.IdentityCardImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.view.ViewIdentityCardImageListener;
import gopdu.pdu.gopduversiondriver.viewmodel.IdentityCardImageViewModel;

public class PresenterIdentityCardImage implements IdentityCardImageResponse {
    private IdentityCardImageViewModel identityCardViewModel;
    private ViewIdentityCardImageListener callback;

    public PresenterIdentityCardImage(ViewIdentityCardImageListener callback) {
        this.identityCardViewModel = new IdentityCardImageViewModel(this);
        this.callback = callback;
    }

    public void getDataImage(int requestCode, int resultCode) {
        identityCardViewModel.getData(requestCode, resultCode);
    }

    public void checkNulImage(Driver driver){
        identityCardViewModel.checkNullImage(driver);
    }

    @Override
    public void onIdentityCardImageFront() {
        callback.onIdentityCardImageFront();
    }

    @Override
    public void onIdentityCardImageBackside() {
        callback.onIdentityCardImageBackside();
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
