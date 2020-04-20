package gopdu.pdu.gopduversiondriver.view;

import gopdu.pdu.gopduversiondriver.object.HistoryDetail;

public interface ViewHistoryDetailActivityListener {
    void getDataHistorySuccess(HistoryDetail data);

    void getDataHistoryFaild();
}
