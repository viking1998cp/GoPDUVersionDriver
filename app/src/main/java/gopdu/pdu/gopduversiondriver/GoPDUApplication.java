package gopdu.pdu.gopduversiondriver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;

import gopdu.pdu.gopduversiondriver.dialog.CheckDialog;

public class GoPDUApplication extends Application {
    private static GoPDUApplication instance;
    private Gson mGSon;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        mGSon=new Gson();

    }
    public static GoPDUApplication getInstance(){
        return instance;
    }
    public Gson getGSon() {
        return mGSon;
    }

    private Boolean checkInternet = false;

    private void check3g() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (isOnline(context)) {
                    checkInternet = true;
                } else {
                    checkInternet = false;
                    Toast.makeText(GoPDUApplication.getInstance(), "Bạn chưa bật internet", Toast.LENGTH_SHORT).show();


                }


            }

            public boolean isOnline(Context context) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        GoPDUApplication.getInstance().registerReceiver(broadcastReceiver, intentFilter);
    }
}
