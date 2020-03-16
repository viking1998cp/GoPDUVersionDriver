package gopdu.pdu.gopduversiondriver.viewmodel;

import android.util.Log;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.modelresponse.IdentityCardImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;

public class IdentityCardImageViewModel {

    IdentityCardImageResponse callback;

    public IdentityCardImageViewModel(IdentityCardImageResponse callback) {
        this.callback = callback;
    }

    public void getData(int requestCode, int resultCode) {
        Log.d("BBB", "getData: "+requestCode+"/"+resultCode);
        if(requestCode== Common.RQUEST_CODE_IDENTITY_CARD_FRONT && resultCode==Common.RQUEST_CODE_IDENTITY_CARD_FRONT){
             callback.onIdentityCardImageFront();
        }else if(requestCode==Common.RQUEST_CODE_IDENTITY_CARD_BACKSIDE && resultCode==Common.RQUEST_CODE_IDENTITY_CARD_BACKSIDE){
            callback.onIdentityCardImageBackside();
        }
    }

    public void checkNullImage(Driver driver) {
        if(driver.getImvIdentitycardFront() != null && driver.getImvIdentitycardBackside() != null){
            callback.notNullImage();
        }else {
            callback.nullImage();
        }
    }
}
