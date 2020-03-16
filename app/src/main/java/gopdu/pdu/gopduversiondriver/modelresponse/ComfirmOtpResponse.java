package gopdu.pdu.gopduversiondriver.modelresponse;

public interface ComfirmOtpResponse {
    void sendOtp(String numberPhone);

    void setUpTimer(long millisUntilFinished);

    void setUpFinishTimer();
    void notNullOtp(String otp);
    void nullOtp();

    void faildLogin();

    void successLogin();

    void faildChange();

    void successChange();
}
