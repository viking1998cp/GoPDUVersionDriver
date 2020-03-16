package gopdu.pdu.gopduversiondriver.modelresponse;

import java.util.ArrayList;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.object.ImageAlbum;

public interface AcceptContractResponse {
    void notNullUrlImage(ArrayList<ImageAlbum> imageRespon);

    void nullUrlImage();

    void getParamUpload(Map<String, String> param);

    void registerSuccess();

    void registerFaild();
}
