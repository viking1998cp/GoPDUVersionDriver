package gopdu.pdu.gopduversiondriver.viewmodel;

import android.util.Log;

import gopdu.pdu.gopduversiondriver.modelresponse.BaseActivityResponse;

public class BaseActivityViewModel {
    BaseActivityResponse callback;

    public BaseActivityViewModel(BaseActivityResponse callback) {
        this.callback = callback;
    }

    public void showDialog(boolean isNetworkState, boolean isLocationState) {
        if(isNetworkState){
            if(isLocationState){
                callback.dimissDialog();
            }else {
                callback.showDialogCheckLocation();
            }
        }else {
            callback.showDialogCheckNet();
        }
    }
}
