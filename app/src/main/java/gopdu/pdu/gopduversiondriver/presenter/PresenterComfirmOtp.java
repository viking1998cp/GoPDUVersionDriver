package gopdu.pdu.gopduversiondriver.presenter;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import gopdu.pdu.gopduversiondriver.modelresponse.ComfirmOtpResponse;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.view.ViewComfirmOtpListener;
import gopdu.pdu.gopduversiondriver.viewmodel.ComfirmOtpViewModel;

public class PresenterComfirmOtp implements ComfirmOtpResponse {

    private ComfirmOtpViewModel comfirmOtpModel;
    private ViewComfirmOtpListener callback;

    public PresenterComfirmOtp(ViewComfirmOtpListener callback) {
        comfirmOtpModel = new ComfirmOtpViewModel(this);
        this.callback = callback;
    }


    public void reciverSetUpView() {
        comfirmOtpModel.setUpView();
    }
    public void reciverCheckNullOtp(String otp) {
        comfirmOtpModel.checkNullOtp(otp);
    }

    public void reciverCheckSuccessLogin(Task<AuthResult> task) {
        comfirmOtpModel.checkSuccessLogin(task);
    }

    public void reciverCheckChangeIdSuccess(ServerResponse serverResponse) {
        comfirmOtpModel.checkChangeIdSuccess(serverResponse);
    }
    @Override
    public void sendOtp(String numberPhone) {
        callback.sendOtp(numberPhone);
    }

    @Override
    public void setUpTimer(long millisUntilFinished) {
        callback.setUpTimer(millisUntilFinished);
    }

    @Override
    public void setUpFinishTimer() {
        callback.setUpFinishTimer();
    }

    @Override
    public void notNullOtp(String otp) {
        callback.notNullOtp(otp);
    }

    @Override
    public void nullOtp() {
        callback.nullOtp();
    }

    @Override
    public void faildLogin() {
        callback.faildLogin();
    }

    @Override
    public void successLogin() {
        callback.successLogin();
    }

    @Override
    public void faildChange() {
        callback.faildChange();
    }

    @Override
    public void successChange() {
        callback.successChange();
    }


}
