package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.repository.CheckExitsAccountRepository;


public class CheckExitsViewModel extends AndroidViewModel {
    public CheckExitsViewModel(Application application){
        super(application);
    }
    public LiveData<ServerResponse> CheckExitsAccount(Map<String, String> params) {
        return CheckExitsAccountRepository.getInstance().getMutableLiveData(params);
    }
}
