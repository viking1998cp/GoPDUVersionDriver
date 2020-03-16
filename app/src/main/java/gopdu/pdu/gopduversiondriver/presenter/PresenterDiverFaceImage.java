package gopdu.pdu.gopduversiondriver.presenter;

import gopdu.pdu.gopduversiondriver.modelresponse.DriverfaceImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.view.ViewDriverFaceImageListener;
import gopdu.pdu.gopduversiondriver.viewmodel.DriverFaceViewModel;

public class PresenterDiverFaceImage  implements DriverfaceImageResponse {

    private DriverFaceViewModel driverFaceModel;
    private ViewDriverFaceImageListener callback;

    public PresenterDiverFaceImage(ViewDriverFaceImageListener callback) {
        driverFaceModel = new DriverFaceViewModel(this);
        this.callback = callback;
    }

    public void getData(int requestCode, int resultCode) {
        driverFaceModel.getData(requestCode, resultCode);
    }

    public void checkNullImage(Driver driver) {
        driverFaceModel.checkNullImage(driver);
    }


    @Override
    public void onDriverFaceImage() {
        callback.onDriverFaceImage();
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
