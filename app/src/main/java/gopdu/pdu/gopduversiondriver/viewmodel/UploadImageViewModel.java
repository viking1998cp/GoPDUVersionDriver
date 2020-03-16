package gopdu.pdu.gopduversiondriver.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.ImageRespon;
import gopdu.pdu.gopduversiondriver.repository.UploadImageRepository;
import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class UploadImageViewModel extends AndroidViewModel {
    public UploadImageViewModel(Application application){
        super(application);
    }
    public LiveData<ImageRespon> uploadImage(@Part MultipartBody.Part licensesPathFrontFile,
                                                   @Part MultipartBody.Part licensesPathBacksideFile,
                                                   @Part MultipartBody.Part identityCardFrontFile,
                                                   @Part MultipartBody.Part identityCardBacksideFile,
                                                   @Part MultipartBody.Part motocyclepaperFrontFile,
                                                   @Part MultipartBody.Part motocyclepaperBacksideFile,
                                                   @Part MultipartBody.Part driverFace) {
        return UploadImageRepository.getInstance().getMutableLiveData(licensesPathFrontFile,
                licensesPathBacksideFile,
                identityCardFrontFile,
                identityCardBacksideFile,
                motocyclepaperFrontFile,
                motocyclepaperBacksideFile,
                driverFace);
    }
}
