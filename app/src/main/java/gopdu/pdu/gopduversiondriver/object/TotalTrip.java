package gopdu.pdu.gopduversiondriver.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TotalTrip implements Serializable {
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("tripsuccess")
    @Expose
    private double tripsuccess;
    @SerializedName("tripcancel")
    @Expose
    private double tripcancel;
    @SerializedName("ratting")
    @Expose
    private double ratting;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getTripsuccess() {
        return tripsuccess;
    }

    public void setTripsuccess(double tripsuccess) {
        this.tripsuccess = tripsuccess;
    }

    public double getTripcancel() {
        return tripcancel;
    }

    public void setTripcancel(double tripcancel) {
        this.tripcancel = tripcancel;
    }

    public double getRatting() {
        return ratting;
    }

    public void setRatting(double ratting) {
        this.ratting = ratting;
    }
}
