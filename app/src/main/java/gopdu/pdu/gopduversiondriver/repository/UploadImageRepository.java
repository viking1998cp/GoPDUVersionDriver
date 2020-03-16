package gopdu.pdu.gopduversiondriver.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.network.ImageRespon;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class UploadImageRepository {
    private static UploadImageRepository instance;

    public static UploadImageRepository getInstance() {
        if (instance == null)
            instance = new UploadImageRepository();
        return instance;
    }

    private MutableLiveData<ImageRespon> mutableLiveData;

    public MutableLiveData<ImageRespon> getMutableLiveData(@Part MultipartBody.Part licensesPathFrontFile,
                                                              @Part MultipartBody.Part licensesPathBacksideFile,
                                                              @Part MultipartBody.Part identityCardFrontFile,
                                                              @Part MultipartBody.Part identityCardBacksideFile,
                                                              @Part MultipartBody.Part motocyclepaperFrontFile,
                                                              @Part MultipartBody.Part motocyclepaperBacksideFile,
                                                              @Part MultipartBody.Part driverFace) {
        mutableLiveData = new MutableLiveData<>();
        try {
            DataService dataService = APIService.getService();
            dataService.uploadMulFile1(licensesPathFrontFile,
                                        licensesPathBacksideFile,
                                        identityCardFrontFile,
                                        identityCardBacksideFile,
                                        motocyclepaperFrontFile,
                                        motocyclepaperBacksideFile,
                                         driverFace).enqueue(new Callback<ImageRespon>() {
                @Override
                public void onResponse(Call<ImageRespon> call, Response<ImageRespon> response) {
                    try {
                        ImageRespon imageRespon = response.body();
                        if (imageRespon.getData() != null) {
                            mutableLiveData.setValue(imageRespon);
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
                public void onFailure(Call<ImageRespon> call, Throwable t) {
                    try {
                        mutableLiveData.setValue(null);
                        Common.ShowToastShort(GoPDUApplication.getInstance().getString(R.string.checkConnect));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("BBB", "onResponse2: "+e.toString());
                    }
                    Log.d("BBB", "onResponse3: "+t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mutableLiveData.setValue(null);
            Log.d("BBB", "onResponse4: "+e.toString());

        }
        return mutableLiveData;
    }
}
