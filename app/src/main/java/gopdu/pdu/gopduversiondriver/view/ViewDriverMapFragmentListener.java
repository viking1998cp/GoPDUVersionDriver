package gopdu.pdu.gopduversiondriver.view;

import android.location.Address;

import gopdu.pdu.gopduversiondriver.object.Driver;

public interface ViewDriverMapFragmentListener {
    void takenInfomationAccount(Driver data);
    void connectWorking();

    void dissconnectWorking();
    void getPickUpName(Address pickup);

    void getDestinationName(Address destination);

    void insertSuccess(String message);

    void insertError(String message);

}
