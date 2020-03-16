package gopdu.pdu.gopduversiondriver.viewmodel;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.modelresponse.MotorcyclepaperImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;

public class MotocyclepaperImageViewModel  {

    MotorcyclepaperImageResponse callback;

    public MotocyclepaperImageViewModel(MotorcyclepaperImageResponse callback) {
        this.callback = callback;
    }

    public void getData(int requestCode, int resultCode) {
        if (requestCode == Common.RQUEST_CODE_MOTORCYCLEPAPER_FRONT && resultCode == Common.RQUEST_CODE_MOTORCYCLEPAPER_FRONT) {
            callback.onMotorcyclepaperImageFront();
        } else if (requestCode == Common.RQUEST_CODE_MOTORCYCLEPAPER_BACKSIDE && resultCode == Common.RQUEST_CODE_MOTORCYCLEPAPER_BACKSIDE) {
            callback.onMotorcyclepaperImageBackside();
        }
    }

    public void checkNull(Driver driver) {
        if(driver.getImvMotorcyclepapersFront() != null && driver.getImvMotorcyclepapersBackside() != null){
            callback.notNullImage();
        }else {
            callback.nullImage();
        }
    }
}
