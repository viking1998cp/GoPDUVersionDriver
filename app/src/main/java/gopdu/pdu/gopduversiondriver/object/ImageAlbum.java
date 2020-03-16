package gopdu.pdu.gopduversiondriver.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hoangdh on 24/09/2016.
 * edit tungda
 */
public class ImageAlbum implements Serializable{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String path;

    public ImageAlbum() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}


