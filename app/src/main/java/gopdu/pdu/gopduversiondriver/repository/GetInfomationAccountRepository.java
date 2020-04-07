package gopdu.pdu.gopduversiondriver.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetInfomationAccountRepository {
    private static GetInfomationAccountRepository instance;

    public static GetInfomationAccountRepository getInstance() {
        if (instance == null)
            instance = new GetInfomationAccountRepository();
        return instance;
    }

    private MutableLiveData<AccountResponse> mutableLiveData;

    public MutableLiveData<AccountResponse> getMutableLiveData(Map<String, String> params) {
        mutableLiveData = new MutableLiveData<>();
        try {
            DataService dataService = APIService.getService();
            dataService.getInfomationAccount(params).enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    try {
                        Log.d("BBB", "onResponse: "+response.body().getData());
                        AccountResponse accountResponse = response.body();
                        if (accountResponse != null) {
                            mutableLiveData.setValue(accountResponse);
                        } else {
                            mutableLiveData.setValue(null);
                            Common.ShowToastShort(accountResponse.getMessage());
                        }


                    } catch (Exception e) {
                        mutableLiveData.setValue(null);
                        e.printStackTrace();
                        Log.d("BBB", "onResponse: "+e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
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
