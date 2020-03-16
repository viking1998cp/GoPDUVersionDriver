package gopdu.pdu.gopduversiondriver.view;

import java.util.ArrayList;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.object.ImageAlbum;

public interface ViewAcceptContractListener {
    void notNullUrlImage(ArrayList<ImageAlbum> imageRespon);

    void nullUrlImage();

    void getParamUpload(Map<String, String> param);
    void registerSuccess();

    void registerFaild();
}
