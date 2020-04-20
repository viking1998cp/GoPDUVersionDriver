package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.HistoryDetailRespon;
import gopdu.pdu.gopduversiondriver.repository.GetHistoryDetailRepository;

public class GetHistoryDetailViewModel extends AndroidViewModel {
    public GetHistoryDetailViewModel(Application application){
        super(application);
    }
    public LiveData<HistoryDetailRespon> getHistoryResponseLiveData(Map<String, String> params) {
        return GetHistoryDetailRepository.getInstance().getMutableLiveData(params);
    }
}
