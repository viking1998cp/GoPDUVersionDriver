package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.repository.TakenInfomationAccountRepository;

public class TakenAccountInfomationViewModel extends AndroidViewModel {
    public TakenAccountInfomationViewModel(Application application){
        super(application);
    }
    public LiveData<AccountResponse> TakenInfomationAccount(Map<String, String> params) {
        return TakenInfomationAccountRepository.getInstance().getMutableLiveData(params);
    }
}
