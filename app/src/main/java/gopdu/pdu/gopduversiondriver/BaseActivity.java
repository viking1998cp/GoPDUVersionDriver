package gopdu.pdu.gopduversiondriver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import gopdu.pdu.gopduversiondriver.dialog.CheckDialog;
import gopdu.pdu.gopduversiondriver.presenter.PresenterBaseActivity;
import gopdu.pdu.gopduversiondriver.view.ViewBaseActivityListener;

public abstract class BaseActivity extends AppCompatActivity implements ViewBaseActivityListener {
    private CheckDialog checkDialog;
    private boolean isNetworkState =false;
    private boolean isLocationState =false;
    private PresenterBaseActivity presenter;

    public abstract int getLayoutId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        registerBroadcastReciver();
        setContentView(getLayoutId());

    }

    private void init() {
        presenter = new PresenterBaseActivity(this);
        checkDialog = new CheckDialog(this, R.style.translucentDialog);
        checkDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        checkDialog.setCancelable(false);
    }


    //Đăng ký broadcast lắng nghe sự kiện thay đổi network
    private void registerBroadcastReciver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            isNetworkState = isOnline(context);
            isLocationState = isLocation(context);
            presenter.reciverShowDialog(isNetworkState, isLocationState);

        }

        public boolean isOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        }
        public boolean isLocation(Context context){
            final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
            if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                return true;
            }
            else
            {
                return  false;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void dimissDialog() {
        checkDialog.dismiss();
    }

    @Override
    public void showDialogCheckNet() {
        checkDialog.show();
        checkDialog.messengerDialog(getString(R.string.checkConnect));

    }

    @Override
    public void showDialogCheckLocation() {
        checkDialog.show();
        checkDialog.messengerDialog(getString(R.string.checkLocation));
    }
}
