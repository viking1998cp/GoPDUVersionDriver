package gopdu.pdu.gopduversiondriver.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAccountRepository {
    private static UploadAccountRepository instance;

    public static UploadAccountRepository getInstance() {
        if (instance == null)
            instance = new UploadAccountRepository();
        return instance;
    }

    private MutableLiveData<ServerResponse> mutableLiveData;

    public MutableLiveData<ServerResponse> getMutableLiveData(Map<String, String> params) {
        mutableLiveData = new MutableLiveData<>();
        try {
            DataService dataService = APIService.getService();
            dataService.registerAccount(params).enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    try {
                        ServerResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            mutableLiveData.setValue(serverResponse);
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
                public void onFailure(Call<ServerResponse> call, Throwable t) {

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
