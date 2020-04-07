package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.TotalTripResponse;
import gopdu.pdu.gopduversiondriver.repository.GetTotalTripRepository;

public class GetTotalTripViewModel extends AndroidViewModel {
    public GetTotalTripViewModel(Application application){
        super(application);
    }
    public LiveData<TotalTripResponse> getTotalTripResponseLiveData(Map<String, String> params) {
        return GetTotalTripRepository.getInstance().getMutableLiveData(params);
    }
}
