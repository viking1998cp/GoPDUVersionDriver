package gopdu.pdu.gopduversiondriver.presenter;

import gopdu.pdu.gopduversiondriver.modelresponse.HistoryDetailActivityResponse;
import gopdu.pdu.gopduversiondriver.network.HistoryDetailRespon;
import gopdu.pdu.gopduversiondriver.object.HistoryDetail;
import gopdu.pdu.gopduversiondriver.view.ViewHistoryDetailActivityListener;
import gopdu.pdu.gopduversiondriver.viewmodel.HistoryDetailActivityViewModel;

public class PresenterHistoryDetailActivity implements HistoryDetailActivityResponse {
    private HistoryDetailActivityViewModel  historyDetailActivityModel;
    private ViewHistoryDetailActivityListener callback;

    public PresenterHistoryDetailActivity(ViewHistoryDetailActivityListener callback) {
        historyDetailActivityModel = new HistoryDetailActivityViewModel(this);
        this.callback = callback;
    }

    public void reciverDataHistoryDetail(HistoryDetailRespon historyDetailRespon) {
        historyDetailActivityModel.getDataHistoryDetail(historyDetailRespon);
    }

    @Override
    public void getDataHistorySuccess(HistoryDetail data) {
        callback.getDataHistorySuccess(data);
    }

    @Override
    public void getDataHistoryFaild() {
        callback.getDataHistoryFaild();
    }
}
