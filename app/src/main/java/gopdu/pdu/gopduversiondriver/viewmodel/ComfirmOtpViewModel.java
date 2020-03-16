package gopdu.pdu.gopduversiondriver.viewmodel;

import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import gopdu.pdu.gopduversiondriver.modelresponse.ComfirmOtpResponse;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;

public class ComfirmOtpViewModel {
    private ComfirmOtpResponse callback;

    public ComfirmOtpViewModel(ComfirmOtpResponse callback) {
        this.callback = callback;
    }

    public void sendOtp(String numberPhone) {
        callback.sendOtp(numberPhone);
    }

    public void setUpView() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                callback.setUpTimer(millisUntilFinished);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                callback.setUpFinishTimer();
            }

        }.start();
    }

    public void checkNullOtp(String otp) {
        if(otp.equals("")){
            callback.nullOtp();
        }else {
            callback.notNullOtp(otp);
        }
    }

    public void checkSuccessLogin(Task<AuthResult> task) {
        if(task.isSuccessful()){
            callback.successLogin();
        }else {
            callback.faildLogin();
        }
    }

    public void checkChangeIdSuccess(ServerResponse serverResponse) {
        if(serverResponse.getSuccess()){
            callback.successChange();
        }else{
            callback.faildChange();
        }
    }
}
