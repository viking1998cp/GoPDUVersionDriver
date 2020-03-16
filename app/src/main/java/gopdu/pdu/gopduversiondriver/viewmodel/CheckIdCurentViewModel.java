package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.repository.CheckIdCurentRepository;

public class CheckIdCurentViewModel extends AndroidViewModel {
    public CheckIdCurentViewModel(Application application){
        super(application);
    }
    public LiveData<ServerResponse> CheckIdCurent(Map<String, String> params) {
        return CheckIdCurentRepository.getInstance().getMutableLiveData(params);
    }
}
