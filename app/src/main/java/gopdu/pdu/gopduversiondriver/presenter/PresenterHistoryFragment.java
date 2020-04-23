package gopdu.pdu.gopduversiondriver.presenter;

import java.util.ArrayList;

import gopdu.pdu.gopduversiondriver.modelresponse.HistoryFragmentResponse;
import gopdu.pdu.gopduversiondriver.network.HistoryResponse;
import gopdu.pdu.gopduversiondriver.object.History;
import gopdu.pdu.gopduversiondriver.view.ViewHistoryFragmentListener;
import gopdu.pdu.gopduversiondriver.viewmodel.HistoryFragmentViewModel;

public class PresenterHistoryFragment implements HistoryFragmentResponse {
    private HistoryFragmentViewModel historyFragmentViewModel;
    private ViewHistoryFragmentListener callback;

    public PresenterHistoryFragment(ViewHistoryFragmentListener callback) {
        historyFragmentViewModel = new HistoryFragmentViewModel(this);
        this.callback = callback;
    }

    public void reciverGetHistory(HistoryResponse historyResponse) {
        historyFragmentViewModel.getHistory(historyResponse);
    }

    public void reciverStatusHistoryChange(int position) {
        historyFragmentViewModel.stateHistoryChange(position);
    }

    @Override
    public void getHistorySuccess(ArrayList<History> data) {
        callback.getHistorySuccess(data);
    }

    @Override
    public void getHistoryFaild() {
        callback.getHistoryFaild();
    }

    @Override
    public void getHistoryStateSuccess(String string) {
        callback.getHistoryStatusSuccess(string);
    }

    @Override
    public void getHistoryStateCancel(String string) {
        callback.getHistoryStatusCancel(string);
    }

    @Override
    public void reciverAllDataHistory() {
        callback.reciverAllDataHistory();
    }


}
