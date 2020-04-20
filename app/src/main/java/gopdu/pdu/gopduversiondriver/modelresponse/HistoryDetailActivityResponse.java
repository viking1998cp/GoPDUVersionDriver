package gopdu.pdu.gopduversiondriver.modelresponse;

import gopdu.pdu.gopduversiondriver.object.HistoryDetail;

public interface HistoryDetailActivityResponse {
    void getDataHistorySuccess(HistoryDetail data);

    void getDataHistoryFaild();
}
