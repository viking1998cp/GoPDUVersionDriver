package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.HistoryResponse;
import gopdu.pdu.gopduversiondriver.network.TotalTripResponse;
import gopdu.pdu.gopduversiondriver.repository.GetHistoryRepository;
import gopdu.pdu.gopduversiondriver.repository.GetTotalTripRepository;

public class GetHistoryViewModel extends AndroidViewModel {
    public GetHistoryViewModel(Application application){
        super(application);
    }
    public LiveData<HistoryResponse> getHistoryResponseLiveData(Map<String, String> params) {
        return GetHistoryRepository.getInstance().getMutableLiveData(params);
    }
}
