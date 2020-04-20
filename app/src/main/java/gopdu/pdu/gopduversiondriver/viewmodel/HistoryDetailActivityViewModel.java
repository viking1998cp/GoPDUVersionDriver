package gopdu.pdu.gopduversiondriver.viewmodel;

import gopdu.pdu.gopduversiondriver.modelresponse.HistoryDetailActivityResponse;
import gopdu.pdu.gopduversiondriver.network.HistoryDetailRespon;

public class HistoryDetailActivityViewModel {
    private HistoryDetailActivityResponse callback;

    public HistoryDetailActivityViewModel(HistoryDetailActivityResponse callback) {
        this.callback = callback;
    }

    public void getDataHistoryDetail(HistoryDetailRespon historyDetailRespon) {
        if(historyDetailRespon.getSuccess() && historyDetailRespon.getData() != null){
            callback.getDataHistorySuccess(historyDetailRespon.getData());
        }else {
            callback.getDataHistoryFaild();
        }
    }
}
