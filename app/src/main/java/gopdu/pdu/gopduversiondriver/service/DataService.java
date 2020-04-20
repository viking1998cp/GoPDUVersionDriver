package gopdu.pdu.gopduversiondriver.service;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.network.AccountResponse;
import gopdu.pdu.gopduversiondriver.network.HistoryDetailRespon;
import gopdu.pdu.gopduversiondriver.network.HistoryResponse;
import gopdu.pdu.gopduversiondriver.network.ImageRespon;
import gopdu.pdu.gopduversiondriver.network.TotalTripResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.object.TotalTrip;
import gopdu.pdu.gopduversiondriver.repository.InsertHistoryRepository;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface DataService {

    @GET("checkExitsAccount.php")
    Call<ServerResponse> checkExits(@QueryMap Map<String,String> params);

    @Multipart
    @POST("upload_multiple_files1.php")
    Call <ImageRespon> uploadMulFile1(@Part MultipartBody.Part licensesPathFrontFile,
                                        @Part MultipartBody.Part licensesPathBacksideFile,
                                        @Part MultipartBody.Part identityCardFrontFile,
                                        @Part MultipartBody.Part identityCardBacksideFile,
                                        @Part MultipartBody.Part motocyclepaperFrontFile,
                                        @Part MultipartBody.Part motocyclepaperBacksideFile,
                                        @Part MultipartBody.Part driverFace);
    @GET("registerDriver.php")
    Call<ServerResponse> registerAccount(@QueryMap Map<String, String> params);

    @GET("checkIdCurrent.php")
    Call<ServerResponse> checkIdCurent(@QueryMap Map<String,String> params);

    @GET("getDriver.php")
    Call<AccountResponse> getInfomationAccount(@QueryMap Map<String,String> params);

    @GET("insertHistory.php")
    Call<ServerResponse> insertHistory (@QueryMap Map<String,String> params);

    @GET("getTotalTripDriver.php")
    Call<TotalTripResponse> getTotalTrip (@QueryMap Map<String,String> params);

    @GET("getHistory.php")
    Call<HistoryResponse> getHistory (@QueryMap Map<String,String> params);

    @GET("getHistoryDetail.php")
    Call<HistoryDetailRespon> getHistoryDetail (@QueryMap Map<String,String> params);



//    @Multipart
//    @POST("upload_multiple_files1.php")
//    Call<ImageRespon> uploadMulFile1(@Part MultipartBody.Part file1,
//                                     @Part MultipartBody.Part file2,
//                                     @Part MultipartBody.Part file3,
//                                     @Part MultipartBody.Part file4,
//                                     @Part MultipartBody.Part file5,
//                                     @Part MultipartBody.Part file6,
//                                     @Part MultipartBody.Part file7);



}
