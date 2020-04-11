package gopdu.pdu.gopduversiondriver.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import gopdu.pdu.gopduversiondriver.object.History;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.object.TotalTrip;

public class HistoryResponse extends ServerResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<History> data;

    public ArrayList<History> getData() {
        return data;
    }

    public void setData(ArrayList<History> data) {
        this.data = data;
    }
}
