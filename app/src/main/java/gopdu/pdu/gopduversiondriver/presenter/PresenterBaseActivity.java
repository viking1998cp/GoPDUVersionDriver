package gopdu.pdu.gopduversiondriver.presenter;

import gopdu.pdu.gopduversiondriver.modelresponse.BaseActivityResponse;
import gopdu.pdu.gopduversiondriver.view.ViewBaseActivityListener;
import gopdu.pdu.gopduversiondriver.viewmodel.BaseActivityViewModel;

public class PresenterBaseActivity implements BaseActivityResponse {
    private BaseActivityViewModel baseActivityModel;
    private ViewBaseActivityListener callback;

    public PresenterBaseActivity(ViewBaseActivityListener callback) {
        baseActivityModel = new BaseActivityViewModel(this);
        this.callback = callback;
    }


    public void reciverShowDialog(boolean isNetworkState, boolean isLocationState) {
        baseActivityModel.showDialog(isNetworkState, isLocationState);
    }

    @Override
    public void dimissDialog() {
        callback.dimissDialog();
    }

    @Override
    public void showDialogCheckNet() {
        callback.showDialogCheckNet();
    }

    @Override
    public void showDialogCheckLocation() {
        callback.showDialogCheckLocation();
    }
}
