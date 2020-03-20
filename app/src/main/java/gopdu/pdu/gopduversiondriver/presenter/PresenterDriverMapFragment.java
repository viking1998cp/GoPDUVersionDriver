package gopdu.pdu.gopduversiondriver.presenter;

import android.util.Log;

import gopdu.pdu.gopduversiondriver.modelresponse.DriverMapFragmentResponse;
import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.view.ViewDriverMapFragmentListener;
import gopdu.pdu.gopduversiondriver.viewmodel.DriverMapFragmentViewModel;

public class PresenterDriverMapFragment implements DriverMapFragmentResponse{
    private DriverMapFragmentViewModel driverMapFragmentModel;
    private ViewDriverMapFragmentListener callback;

    public PresenterDriverMapFragment(ViewDriverMapFragmentListener callback) {
        driverMapFragmentModel = new DriverMapFragmentViewModel(this);
        this.callback = callback;
    }

    public void reciverInfomationAccount(AccountResponse accountResponse) {
        driverMapFragmentModel.takenInfomationAccount(accountResponse);
    }

    @Override
    public void takenInfomationAccount(Driver data) {
        callback.takenInfomationAccount(data);
    }
}
