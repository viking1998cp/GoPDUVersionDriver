package gopdu.pdu.gopduversiondriver.viewmodel;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.modelresponse.LicenseDriverImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;

public class LicenseDriverImageViewModel {

    LicenseDriverImageResponse callback;

    public LicenseDriverImageViewModel(LicenseDriverImageResponse callback) {
        this.callback = callback;
    }

    public void getData(int requestCode, int resultCode) {
        if(requestCode== Common.RQUEST_CODE_LICENSE_FRONT && resultCode==Common.RQUEST_CODE_LICENSE_FRONT){
            callback.onLicenseDriverImageFront();
        }else if(requestCode==Common.RQUEST_CODE_LICENSE_BACK && resultCode==Common.RQUEST_CODE_LICENSE_BACK){
            callback.onLicenseDriverImageBackside();
        }
    }

    public void checkNullImage(Driver driver) {
        if(driver.getImvLicensedriverFront() != null && driver.getImvLicensedriverBackside() != null){
            callback.notNullImage();
        }else {
            callback.nullImage();
        }
    }
}
