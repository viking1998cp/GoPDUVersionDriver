package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.repository.UploadAccountRepository;

public class UploadAccountViewModel extends AndroidViewModel {
    public UploadAccountViewModel(Application application){
        super(application);
    }
    public LiveData<ServerResponse> uploadAccount(Map<String, String> params) {
        return UploadAccountRepository.getInstance().getMutableLiveData(params);
    }
}
