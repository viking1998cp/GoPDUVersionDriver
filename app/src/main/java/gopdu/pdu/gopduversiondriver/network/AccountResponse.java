package gopdu.pdu.gopduversiondriver.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;

public class AccountResponse extends ServerResponse {
    @SerializedName("data")
    @Expose
    private Driver data;

    public Driver getData() {
        return data;
    }

    public void setData(Driver data) {
        this.data = data;
    }
}
