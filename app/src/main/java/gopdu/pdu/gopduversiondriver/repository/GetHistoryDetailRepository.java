package gopdu.pdu.gopduversiondriver.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.network.HistoryDetailRespon;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetHistoryDetailRepository {

    private static GetHistoryDetailRepository instance;

    public static GetHistoryDetailRepository getInstance() {
        if (instance == null)
            instance = new GetHistoryDetailRepository();
        return instance;
    }

    private MutableLiveData<HistoryDetailRespon> mutableLiveData;

    public MutableLiveData<HistoryDetailRespon> getMutableLiveData(Map<String, String> params) {
        mutableLiveData = new MutableLiveData<>();
        try {
            DataService dataService = APIService.getService();
            dataService.getHistoryDetail(params).enqueue(new Callback<HistoryDetailRespon>() {
                @Override
                public void onResponse(Call<HistoryDetailRespon> call, Response<HistoryDetailRespon> response) {
                    try {
                        HistoryDetailRespon historyDetailRespon = response.body();
                        if (historyDetailRespon.getData() != null) {
                            mutableLiveData.setValue(historyDetailRespon);
                        } else {
                            mutableLiveData.setValue(null);
                            Common.ShowToastShort(GoPDUApplication.getInstance().getString(R.string.checkConnect));
                        }

                    } catch (Exception e) {
                        mutableLiveData.setValue(null);
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<HistoryDetailRespon> call, Throwable t) {

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
