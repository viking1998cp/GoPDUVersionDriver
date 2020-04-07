package gopdu.pdu.gopduversiondriver.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.network.TotalTripResponse;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTotalTripRepository {
    private static GetTotalTripRepository instance;

    public static GetTotalTripRepository getInstance() {
        if (instance == null)
            instance = new GetTotalTripRepository();
        return instance;
    }

    private MutableLiveData<TotalTripResponse> mutableLiveData;

    public MutableLiveData<TotalTripResponse> getMutableLiveData(Map<String, String> params) {
        mutableLiveData = new MutableLiveData<>();
        try {
            DataService dataService = APIService.getService();
            dataService.getTotalTrip(params).enqueue(new Callback<TotalTripResponse>() {
                @Override
                public void onResponse(Call<TotalTripResponse> call, Response<TotalTripResponse> response) {
                    try {
                        Log.d("BBB", "onResponse: "+response.body().getData());
                        TotalTripResponse totalTripResponse = response.body();
                        if (totalTripResponse != null) {
                            mutableLiveData.setValue(totalTripResponse);
                        } else {
                            mutableLiveData.setValue(null);
                        }


                    } catch (Exception e) {
                        mutableLiveData.setValue(null);
                        e.printStackTrace();
                        Log.d("BBB", "onResponse: "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<TotalTripResponse> call, Throwable t) {
                    Log.d("BBB", "onResponse: "+t.getMessage());
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
            Log.d("BBB", "onResponse1: "+e.getMessage());

        }
        return mutableLiveData;
    }
}
