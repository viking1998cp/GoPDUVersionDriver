package gopdu.pdu.gopduversiondriver.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.object.TotalTrip;

public class TotalTripResponse  extends ServerResponse {
    @SerializedName("data")
    @Expose
    private TotalTrip data;

    public TotalTrip getData() {
        return data;
    }

    public void setData(TotalTrip data) {
        this.data = data;
    }
}
