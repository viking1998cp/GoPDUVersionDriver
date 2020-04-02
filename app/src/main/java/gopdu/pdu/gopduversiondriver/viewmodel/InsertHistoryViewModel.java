package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.repository.InsertHistoryRepository;

public class InsertHistoryViewModel extends AndroidViewModel {
    public InsertHistoryViewModel(Application application){
        super(application);
    }
    public LiveData<ServerResponse> insertHistory(Map<String, String> params) {
        return InsertHistoryRepository.getInstance().getMutableLiveData(params);
    }
}
