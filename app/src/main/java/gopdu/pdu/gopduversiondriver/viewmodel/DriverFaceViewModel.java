package gopdu.pdu.gopduversiondriver.viewmodel;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.modelresponse.DriverfaceImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;

public class DriverFaceViewModel {
    private DriverfaceImageResponse callback;

    public DriverFaceViewModel(DriverfaceImageResponse callback) {
        this.callback = callback;
    }

    public void getData(int requestCode, int resultCode) {
        if (requestCode == Common.RQUEST_CODE_DRIVER_FACE && resultCode == Common.RQUEST_CODE_DRIVER_FACE) {
           callback.onDriverFaceImage();
        }
    }

    public void checkNullImage(Driver driver) {
        if(driver.getImvDriverface() != null){
            callback.notNullImage();
        }else {
            callback.nullImage();
        }
    }
}
