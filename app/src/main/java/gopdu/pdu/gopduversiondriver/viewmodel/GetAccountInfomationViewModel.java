package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.repository.GetInfomationAccountRepository;

public class GetAccountInfomationViewModel extends AndroidViewModel {
    public GetAccountInfomationViewModel(Application application){
        super(application);
    }
    public LiveData<AccountResponse> TakenInfomationAccount(Map<String, String> params) {
        return GetInfomationAccountRepository.getInstance().getMutableLiveData(params);
    }
}
