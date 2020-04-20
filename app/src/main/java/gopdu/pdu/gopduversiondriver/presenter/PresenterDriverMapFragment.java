package gopdu.pdu.gopduversiondriver.presenter;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import gopdu.pdu.gopduversiondriver.modelresponse.DriverMapFragmentResponse;
import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.network.TotalTripResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.object.TotalTrip;
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
    public void reciverWorking(boolean isChecked) {
        driverMapFragmentModel.working(isChecked);
    }

    public void reciverPickUpname(LatLng pickupLatLng) {
        driverMapFragmentModel.getPickUpName(pickupLatLng);
    }

    public void reciverDestinationName(LatLng destinaLatLng) {
        driverMapFragmentModel.getDestinationName(destinaLatLng);
    }

    public void reciverInsertHistory(ServerResponse serverResponse) {
        driverMapFragmentModel.insertHistory(serverResponse);
    }
    public void reciverRating(TotalTripResponse totalTripResponse) {
        driverMapFragmentModel.getRatting(totalTripResponse);
    }


    @Override
    public void takenInfomationAccount(Driver data) {
        callback.takenInfomationAccount(data);
    }

    @Override
    public void connectWorking() {
        callback.connectWorking();
    }

    @Override
    public void dissconnectWorking() {
        callback.dissconnectWorking();
    }

    @Override
    public void getPickUpName(Address pickup) {
        callback.getPickUpName(pickup);
    }

    @Override
    public void getDestinationName(Address destination) {
        callback.getDestinationName(destination);

    }

    @Override
    public void insertSuccess(String message) {
        callback.insertSuccess(message);
    }

    @Override
    public void insertError(String message) {
        callback.insertError(message);

    }

    @Override
    public void showRatting(TotalTrip totalTrip, double acceptPercent, double cancelPercent) {
        callback.showRating(totalTrip, acceptPercent, cancelPercent);
    }


}
