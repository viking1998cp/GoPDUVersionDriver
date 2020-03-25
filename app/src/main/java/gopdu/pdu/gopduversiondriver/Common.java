package gopdu.pdu.gopduversiondriver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import gopdu.pdu.gopduversiondriver.dialog.CheckDialog;

/**
 * Created by manh thắng 98.
 */

public class Common {

    public static final int RQUEST_CODE_LICENSE_FRONT = 1;
    public static final int RQUEST_CODE_LICENSE_BACK = 2;
    public static final int RQUEST_CODE_DRIVER_FACE = 3;
    public static final int RQUEST_CODE_IDENTITY_CARD_FRONT = 5;
    public static final int RQUEST_CODE_IDENTITY_CARD_BACKSIDE = 6;
    public static final int RQUEST_CODE_MOTORCYCLEPAPER_FRONT = 7;
    public static final int RQUEST_CODE_MOTORCYCLEPAPER_BACKSIDE = 8;


    //date now
    public static String getNgayHienTai() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(Calendar.getInstance().getTime());
    }

    //Show toast long
    public static void ShowToastLong(String msg) {
        if (Strings.isEmptyOrWhitespace(msg)) {
            msg = "Có lỗi xảy ra, vui lòng kểm tra kết nối internet";
        }
        try {
            Toast toast = Toast.makeText(GoPDUApplication.getInstance(), msg, Toast.LENGTH_LONG);
            centerText(toast.getView());
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Show toast short
    public static void ShowToastShort(String msg) {
        if (Strings.isEmptyOrWhitespace(msg)) {
            msg = "Có lỗi xảy ra, vui lòng kểm tra kết nối internet";
        }
        try {
            Toast toast = Toast.makeText(GoPDUApplication.getInstance(), msg, Toast.LENGTH_SHORT);
            centerText(toast.getView());
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void centerText(View view) {
        if( view instanceof TextView){
            ((TextView) view).setGravity(Gravity.CENTER);
        }else if( view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            int n = group.getChildCount();
            for( int i = 0; i<n; i++ ){
                centerText(group.getChildAt(i));
            }
        }
    }

    //Check connect
    public static boolean checkConnect(){
        boolean haveConnectWfi = false;
        boolean haveConnectMobile = false;
        ConnectivityManager cm =(ConnectivityManager) GoPDUApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for(NetworkInfo ni: networkInfos){
            if(ni.getTypeName().equalsIgnoreCase("WIFI")){
                if(ni.isConnected()){
                    haveConnectWfi = true;
                }
            }
            if(ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if(ni.isConnected()){
                    haveConnectMobile = true;
                }
            }
        }
        return haveConnectMobile || haveConnectWfi;
    }

    //Format to phone number
    public static String formatPhoneNumber(String phone){
        String newphone="+84";
        if(phone.startsWith("0")){
            newphone = newphone + phone.substring(1,phone.length());
            return  newphone;
        }else if(phone.length()==9){
            newphone = newphone + phone;
            return  newphone;
        }else {
            return phone;
        }
    }


    public static float getDistance(LatLng destinationLng, LatLng pickupLng){
        return (float) SphericalUtil.computeDistanceBetween(destinationLng, pickupLng);
    }


    //Hide keyboard
    public static void softInput(Activity activity, boolean show) {
        try {
            if (activity == null || activity.isFinishing()) return;
            Window window = activity.getWindow();
            if (window == null) return;
            View view = window.getCurrentFocus();
            //give decorView a chance
            if (view == null) view = window.getDecorView();
            if (view == null) return;

            InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null || !imm.isActive()) return;
            if(show){
                imm.showSoftInputFromInputMethod(view.getWindowToken(),0);
            }else {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //Lấy bitmap của ảnh  trong bộ nhớ
    public static Bitmap returnBitmapFromStorage(String path){
        File imgFile = new File(path);
        Ion.getDefault(GoPDUApplication.getInstance()).getBitmapCache().clear();
        if(imgFile.exists()){

            BitmapFactory.Options options = new BitmapFactory.Options();
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            //The new size we want to scale to
            final int REQUIRED_WIDTH=720;
            final int REQUIRED_HIGHT=1280;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), o2);

            return myBitmap;
        }
        return  null;
    }


    //Check gps
    public static boolean isGpsEnabled(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        // Find out what the settings say about which providers are enabled
        //  String locationMode = "Settings.Secure.LOCATION_MODE_OFF";
        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        if (mode != Settings.Secure.LOCATION_MODE_OFF) {
            return true;

        } else {
            return false;
        }
    }

    //getAddress
    public  static Address getAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(
                GoPDUApplication.getInstance(), Locale.getDefault());
        Address obj = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            obj = addresses.get(0);

            return  obj;

            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(GoPDUApplication.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return obj;
    }
    //format vnd
    public static String formatVNĐ(int price){
        DecimalFormat formatter = new DecimalFormat(GoPDUApplication.getInstance().getString(R.string.patternVNĐ));
        return formatter.format(price);

    }
}
