package gopdu.pdu.gopduversiondriver.presenter;

import gopdu.pdu.gopduversiondriver.modelresponse.RegisterAccountResponse;
import gopdu.pdu.gopduversiondriver.modelresponse.RegisterResultResponse;
import gopdu.pdu.gopduversiondriver.view.ViewAcceptContractListener;
import gopdu.pdu.gopduversiondriver.view.ViewRegisterResultListener;
import gopdu.pdu.gopduversiondriver.viewmodel.RegisterAccountViewModel;
import gopdu.pdu.gopduversiondriver.viewmodel.RegisterResultViewModel;

public class PresenterRegisterResultActivity implements RegisterResultResponse {
    private RegisterResultViewModel registerResultModel;
    private ViewRegisterResultListener callback;

    public PresenterRegisterResultActivity(ViewRegisterResultListener callback) {
        registerResultModel = new RegisterResultViewModel(this);
        this.callback = callback;
    }

    public void reciverShowResult(String result) {
        registerResultModel.showResult(result);
    }

    @Override
    public void showFaild() {
        callback.showFaild();
    }

    @Override
    public void showSuccess() {
        callback.showSuccess();
    }
}
