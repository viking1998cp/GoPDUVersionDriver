package gopdu.pdu.gopduversiondriver.viewmodel;

import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.modelresponse.DriverMapFragmentResponse;
import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;

public class DriverMapFragmentViewModel {
    private DriverMapFragmentResponse callback;

    public DriverMapFragmentViewModel(DriverMapFragmentResponse callback) {
        this.callback = callback;
    }

    public void takenInfomationAccount(AccountResponse accountResponse) {
        Log.d("BBB", "takenInfomationAccount: "+accountResponse.getSuccess());
        if(accountResponse.getSuccess()){
            callback.takenInfomationAccount(accountResponse.getData());
        }else {
            Common.ShowToastShort(GoPDUApplication.getInstance().getString(R.string.checkConnect));
        }
    }

    public void working(boolean isChecked) {
        if(isChecked){
            callback.connectWorking();
        }else {
            callback.dissconnectWorking();
        }
    }

    public void getPickUpName(LatLng pickupLatLng) {

        Address pickup = Common.getAddress(pickupLatLng.latitude, pickupLatLng.longitude);
        callback.getPickUpName(pickup);
    }

    public void getDestinationName(LatLng destinaLatLng) {
        Address destination = Common.getAddress(destinaLatLng.latitude, destinaLatLng.longitude);
        callback.getDestinationName(destination);
    }

    public void insertHistory(ServerResponse serverResponse) {
        if(serverResponse.getSuccess()){
            callback.insertSuccess(serverResponse.getMessage());
        }else {
            callback.insertError(serverResponse.getMessage());
        }
    }
}
