package gopdu.pdu.gopduversiondriver.viewmodel;

import android.util.Log;

import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.modelresponse.RegisterResultResponse;

public class RegisterResultViewModel {
    RegisterResultResponse callback;

    public RegisterResultViewModel(RegisterResultResponse callback) {
        this.callback = callback;
    }

    public void showResult(String result) {
        Log.d("BBB", "showResult: "+result);
        if(result.equals(GoPDUApplication.getInstance().getString(R.string.registerSuccess))){
            callback.showSuccess();
        }else {
            callback.showFaild();
        }
    }
}
