package gopdu.pdu.gopduversiondriver.presenter;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.modelresponse.MainActivityResponse;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.view.ViewMainActivityListener;
import gopdu.pdu.gopduversiondriver.viewmodel.MainActivityViewModel;

public class PresenterMainActivity implements MainActivityResponse {

    private MainActivityViewModel mainActivityModel;
    private ViewMainActivityListener callback;

    public PresenterMainActivity(ViewMainActivityListener callback) {
        mainActivityModel = new MainActivityViewModel(this);
        this.callback = callback;
    }

    public void reciverNullPhoneNumber(String phone) {
        mainActivityModel.checkNullPhoneNumber(phone);
    }

    public void reciverCheckExits(ServerResponse serverResponse) {
        mainActivityModel.checkExits(serverResponse);
    }

    @Override
    public void nullPhoneNumber() {
        callback.nullPhoneNumber();
    }

    @Override
    public void notNullPhoneNumber(Map<String, String> param) {
        callback.notNullPhoneNumber(param);
    }

    @Override
    public void avalidAccount() {
        callback.avalidAccount();
    }

    @Override
    public void notAvalidAccount(String message) {
        callback.notAvalidAccount(message);
    }

    @Override
    public void logged() {
        callback.logged();
    }

    @Override
    public void unLogged() {
        callback.unLogged();
    }


    public void reciverCheckLogged(FirebaseAuth firebaseAuth) {
        mainActivityModel.checkLogged(firebaseAuth);
    }
}
