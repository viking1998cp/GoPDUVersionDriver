package gopdu.pdu.gopduversiondriver.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gopdu.pdu.gopduversiondriver.object.ImageAlbum;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;

public class ImageRespon extends ServerResponse {
    @SerializedName("image")
    @Expose
    private ArrayList<ImageAlbum> data;

    public ArrayList<ImageAlbum> getData() {
        return data;
    }

    public void setData(ArrayList<ImageAlbum> data) {
        this.data = data;
    }
}
