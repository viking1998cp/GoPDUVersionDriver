package gopdu.pdu.gopduversiondriver.view;

import java.util.ArrayList;

import gopdu.pdu.gopduversiondriver.object.History;

public interface ViewHistoryFragmentListener {

    void getHistorySuccess(ArrayList<History> data);

    void getHistoryFaild();
    void getHistoryStatusSuccess(String string);

    void getHistoryStatusCancel(String string);

    void reciverAllDataHistory();
}
