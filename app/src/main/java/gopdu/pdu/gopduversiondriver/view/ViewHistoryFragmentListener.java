package gopdu.pdu.gopduversiondriver.view;

import java.util.ArrayList;

import gopdu.pdu.gopduversiondriver.object.History;

public interface ViewHistoryFragmentListener {

    void getHistorySuccess(ArrayList<History> data);

    void getHistoryFaild();
    void getHistoryStateSuccess(String string);

    void getHistoryStateCancel(String string);

    void reciverAllDataHistory();
}
