package gopdu.pdu.gopduversiondriver.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.network.HistoryResponse;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetHistoryRepository {

    private static GetHistoryRepository instance;

    public static GetHistoryRepository getInstance() {
        if (instance == null)
            instance = new GetHistoryRepository();
        return instance;
    }

    private MutableLiveData<HistoryResponse> mutableLiveData;

    public MutableLiveData<HistoryResponse> getMutableLiveData(Map<String, String> params) {
        mutableLiveData = new MutableLiveData<>();
        try {
            DataService dataService = APIService.getService();
            dataService.getHistory(params).enqueue(new Callback<HistoryResponse>() {
                @Override
                public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                    Log.d("BBB", "onResponse: "+response.message());
                    Log.d("BBB", "onResponse: "+call.request());
                    try {
                        HistoryResponse historyResponse = response.body();
                        if (historyResponse != null) {
                            mutableLiveData.setValue(historyResponse);
                        } else {
                            mutableLiveData.setValue(null);
                            Common.ShowToastShort(historyResponse.getMessage());
                        }


                    } catch (Exception e) {
                        mutableLiveData.setValue(null);
                        e.printStackTrace();

                    }

                }

                @Override
                public void onFailure(Call<HistoryResponse> call, Throwable t) {
                    try {
                        mutableLiveData.setValue(null);
                        Common.ShowToastShort(GoPDUApplication.getInstance().getString(R.string.checkConnect));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mutableLiveData.setValue(null);

        }
        return mutableLiveData;
    }
}
