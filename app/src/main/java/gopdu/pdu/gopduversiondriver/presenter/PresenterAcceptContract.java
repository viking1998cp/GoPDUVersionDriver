package gopdu.pdu.gopduversiondriver.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.modelresponse.AcceptContractResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.view.ViewAcceptContractListener;
import gopdu.pdu.gopduversiondriver.viewmodel.AcceptContractViewModel;

public class PresenterAcceptContract implements AcceptContractResponse {

    private AcceptContractViewModel acceptContractModel;
    private ViewAcceptContractListener callback;

    public PresenterAcceptContract(ViewAcceptContractListener callback) {
        acceptContractModel = new AcceptContractViewModel(this);
        this.callback = callback;
    }

    public void checkImageUrl(ArrayList<ImageAlbum> imageUrl) {
        acceptContractModel.checkImageUrl(imageUrl);
    }

    @Override
    public void notNullUrlImage(ArrayList<ImageAlbum> imageUrl) {
        callback.notNullUrlImage(imageUrl);
    }

    @Override
    public void nullUrlImage() {
        callback.nullUrlImage();
    }

    @Override
    public void getParamUpload(Map<String, String> param) {
        callback.getParamUpload(param);
    }

    @Override
    public void registerSuccess() {
        callback.registerSuccess();
    }

    @Override
    public void registerFaild() {
        callback.registerFaild();
    }

    public void getParamUpload(Driver driver, ArrayList<ImageAlbum> imageUrl) {
        acceptContractModel.getParamUpload(driver, imageUrl);
    }

    public void checkUploadAccount(boolean success) {
        acceptContractModel.checkUploadAccount(success);
    }
}
