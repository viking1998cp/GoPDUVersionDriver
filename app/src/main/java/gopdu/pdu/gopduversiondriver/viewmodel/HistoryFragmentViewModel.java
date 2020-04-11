package gopdu.pdu.gopduversiondriver.viewmodel;

import android.util.Log;

import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.modelresponse.HistoryFragmentResponse;
import gopdu.pdu.gopduversiondriver.network.HistoryResponse;

public class HistoryFragmentViewModel {
    private HistoryFragmentResponse callback;

    public HistoryFragmentViewModel(HistoryFragmentResponse callback) {
        this.callback = callback;
    }

    public void getHistory(HistoryResponse historyResponse) {
        if(historyResponse.getSuccess()){
            if(historyResponse.getData().size() >0){
                callback.getHistorySuccess(historyResponse.getData());
            }else {
                Log.d("BBB", "getHistory: all data");
                callback.reciverAllDataHistory();
            }

        }else {
            callback.getHistoryFaild();
        }
    }

    public void stateHistoryChange(int position) {
        if(position==0){
            callback.getHistoryStateSuccess(GoPDUApplication.getInstance().getString(R.string.param_StateSuccess));
        }else {
            callback.getHistoryStateCancel(GoPDUApplication.getInstance().getString(R.string.param_StateCancel));
        }
    }
}
