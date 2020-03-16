package gopdu.pdu.gopduversiondriver.viewmodel;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.modelresponse.MainActivityResponse;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;

public class MainActivityViewModel {
    private MainActivityResponse callback;

    public MainActivityViewModel(MainActivityResponse callback) {
        this.callback = callback;
    }

    public void checkNullPhoneNumber(String phone) {
        if(phone.equals("")){
            callback.nullPhoneNumber();
        }else {
            Map<String, String> param = new HashMap<>();
            param.put(GoPDUApplication.getInstance().getString(R.string.paramDriverOrCustomer) ,GoPDUApplication.getInstance().getString(R.string.objectDriver));
            param.put(GoPDUApplication.getInstance().getString(R.string.paramNumberPhone), Common.formatPhoneNumber(phone));
            callback.notNullPhoneNumber(param);
        }
    }

    public void checkExits(ServerResponse serverResponse) {
        Log.d("BBB", "checkExits: "+serverResponse.getMessage());
        if(serverResponse.getSuccess()){
            callback.avalidAccount();
        }else {
            callback.notAvalidAccount(serverResponse.getMessage());
        }
    }

    public void checkLogged(FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getUid()!= null){
            callback.logged();
        }else {
            callback.unLogged();
        }
    }
}
