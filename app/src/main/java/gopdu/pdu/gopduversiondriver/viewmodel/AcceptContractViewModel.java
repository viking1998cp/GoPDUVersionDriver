package gopdu.pdu.gopduversiondriver.viewmodel;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.modelresponse.AcceptContractResponse;
import gopdu.pdu.gopduversiondriver.network.ImageRespon;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;

public class AcceptContractViewModel {
    AcceptContractResponse callback;

    public AcceptContractViewModel(AcceptContractResponse callback) {
        this.callback = callback;
    }

    public void checkImageUrl(ArrayList<ImageAlbum> imageUrl) {
        if(imageUrl.size() != 0){
            callback.notNullUrlImage(imageUrl);
        }else {
            callback.nullUrlImage();
        }
    }

    public void getParamUpload(Driver driver, ArrayList<ImageAlbum> imageUrl) {
        for(int i=0;i<imageUrl.size();i++){
            if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.licenseDriverFront))){
                driver.setImvLicensedriverFront(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.licenseDriverBackSide))){
                driver.setImvLicensedriverBackside(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.identityFront))){
                driver.setImvIdentitycardFront(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.identityBackSide))){
                driver.setImvIdentitycardBackside(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.licenseDriverBackSide))){
                driver.setImvLicensedriverBackside(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.motorcyclepapersFront))){
                driver.setImvMotorcyclepapersFront(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.motorcyclepapersBackSide))){
                driver.setImvMotorcyclepapersBackside(imageUrl.get(i).getPath());
            }else if(imageUrl.get(i).getType().equals(GoPDUApplication.getInstance().getString(R.string.driverFace))){
                driver.setImvDriverface(imageUrl.get(i).getPath());
            }
        }
        Map<String, String> param = new HashMap<>();
        Log.d("BBB", "getParamUpload: "+driver.getImvMotorcyclepapersBackside());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramID), driver.getNumberphone());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramName), driver.getName());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramNumberPhone), driver.getNumberphone());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramGender), driver.getGender());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramBirthDate), driver.getBirthdate());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramLicenseplate), driver.getLicenseplates());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramImv_Driverface), driver.getImvDriverface());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramImv_IdentityCardFront), driver.getImvIdentitycardFront());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramImv_IdentityCardBackside), driver.getImvIdentitycardBackside());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramLicense_DriverFront), driver.getImvLicensedriverFront());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramLicense_DriverBackside), driver.getImvLicensedriverBackside());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramMotocyclepaper_DriverFront), driver.getImvMotorcyclepapersFront());
        param.put(GoPDUApplication.getInstance().getString(R.string.paramMotocyclepaper_DriverBackside), driver.getImvMotorcyclepapersBackside());
        callback.getParamUpload(param);

    }

    public void checkUploadAccount(boolean success) {
        if(success){
            callback.registerSuccess();
        }else {
            callback.registerFaild();
        }
    }
}
