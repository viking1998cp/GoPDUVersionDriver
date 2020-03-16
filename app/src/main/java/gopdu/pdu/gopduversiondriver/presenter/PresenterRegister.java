package gopdu.pdu.gopduversiondriver.presenter;

import java.util.HashMap;
import java.util.Map;


import gopdu.pdu.gopduversiondriver.modelresponse.RegisterAccountResponse;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.view.ViewRegisterListener;
import gopdu.pdu.gopduversiondriver.viewmodel.RegisterAccountViewModel;

public class PresenterRegister implements RegisterAccountResponse {
    private RegisterAccountViewModel registerAccountModel;
    private ViewRegisterListener callback;
    public PresenterRegister(ViewRegisterListener callback) {
        registerAccountModel = new RegisterAccountViewModel(this);
        this.callback = callback;
    }

    public void checkExits(boolean exits){
        registerAccountModel.checkExits(exits);
    }

    public void checknull(String name, String phone,  String birthdate, String gender, String licenseplates){
        registerAccountModel.checkEmpty(name, phone, birthdate, gender, licenseplates);
    }

    @Override
    public void onExits() {
        callback.onExits();

    }

    @Override
    public void onNotExits() {
        callback.onNotExits();
    }

    @Override
    public void onEmpty() {
        callback.onEmpty();
    }

    @Override
    public void onNotEmpty() {
        callback.onNotEmpty();
    }
}
