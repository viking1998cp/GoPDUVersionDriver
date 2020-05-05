package gopdu.pdu.gopduversiondriver.view;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.TotalTrip;

public interface ViewDriverMapFragmentListener {
    void takenInfomationAccount(Driver data);
    void connectWorking();

    void dissconnectWorking();
    void getPickUpName(Address pickup);

    void getDestinationName(Address destination);

    void insertSuccess(String message);

    void insertError(String message);

    void showRating(TotalTrip totalTrip, double acceptPercent, double cancelPercent);

    void resumTripPayment();

    void resumTripDropOff();

    void resumeTripPickUp();

    void resumTripWaitting();
}
