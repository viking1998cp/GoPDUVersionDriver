package gopdu.pdu.gopduversiondriver.modelresponse;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import gopdu.pdu.gopduversiondriver.object.Driver;

public interface DriverMapFragmentResponse {
    void takenInfomationAccount(Driver data);

    void connectWorking();

    void dissconnectWorking();


    void getPickUpName(Address pickup);

    void getDestinationName(Address destination);
}
