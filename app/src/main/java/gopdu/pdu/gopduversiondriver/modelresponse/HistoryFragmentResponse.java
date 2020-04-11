package gopdu.pdu.gopduversiondriver.modelresponse;

import java.util.ArrayList;

import gopdu.pdu.gopduversiondriver.object.History;

public interface HistoryFragmentResponse {
    void getHistorySuccess(ArrayList<History> data);

    void getHistoryFaild();

    void getHistoryStateSuccess(String string);

    void getHistoryStateCancel(String string);

    void reciverAllDataHistory();
}
