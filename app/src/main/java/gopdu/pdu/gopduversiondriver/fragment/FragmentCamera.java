package gopdu.pdu.gopduversiondriver.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.activity.RegisterActivity;
import gopdu.pdu.gopduversiondriver.custom_camera.CameraPreview;
import gopdu.pdu.gopduversiondriver.databinding.FragmentCameraBinding;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;

/**
 * Created by manh thắng 98.
 */

public class FragmentCamera extends Fragment implements Camera.AutoFocusCallback {

    private FragmentCameraBinding binding;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;

    private ProgressDialog progressDialog;
    public static Bitmap bitmap;
    private ImageAlbum image;

    private ImageView imgPreview;
    private MediaPlayer mp;

    private String stateFlash = GoPDUApplication.getInstance().getString(R.string.off);
    private int witdh, height;
    private static final int PICTURE_QUALITY = 100;
    private final int ORIENTATION_CAMERA = 90;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false);
        binding.imvFlash.setBackgroundResource(R.drawable.ic_flash_off);

        mCamera =  Camera.open();
        mCamera.setDisplayOrientation(ORIENTATION_CAMERA);

        mPreview = new CameraPreview(GoPDUApplication.getInstance(), mCamera);
        binding.cPreview.addView(mPreview);

        mCamera.startPreview();

        setUpOnclick();
        initDialog();
        return binding.getRoot();

    }

    private void initDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(GoPDUApplication.getInstance().getString(R.string.waitingProcess));
        progressDialog.setCancelable(false);
    }

    private void setUpOnclick() {

        //Take a photo click
        binding.imvTakeAPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        progressDialog.show();
                        mPreview.getmCamera().takePicture(shutterCallback, null, jpegCallback);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        //Cancel take image
        binding.imvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //Flash on or off
        binding.imvFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stateFlash==GoPDUApplication.getInstance().getString(R.string.off)){
                    stateFlash = GoPDUApplication.getInstance().getString(R.string.on);
                    binding.imvFlash.setBackgroundResource(R.drawable.ic_flash_on);
                    initCamera();
                }else {
                    stateFlash = GoPDUApplication.getInstance().getString(R.string.off);
                    binding.imvFlash.setBackgroundResource(R.drawable.ic_flash_off);
                    initCamera();
                }
            }
        });

        //Focus camera
        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if( event.getAction() == MotionEvent.ACTION_DOWN ) {

                    // Get the pointer's current position
                    float x = event.getX();
                    float y = event.getY();
                    float touchMajor = event.getTouchMajor();
                    float touchMinor = event.getTouchMinor();

                    Rect touchRect = new Rect(
                            (int)(x - touchMajor/2),
                            (int)(y - touchMinor/2),
                            (int)(x + touchMajor/2),
                            (int)(y + touchMinor/2));

                    touchFocusCamera(touchRect);

                    mp = MediaPlayer.create(getContext(), R.raw.camera_focusing);
                    mp.start();
                }

                return true;

            }
        });

    }

    public void initCamera(){
        try {

            mPreview.setFlashMode(stateFlash);// on bat, off tat, auto tu dong bat den flash
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Common.ShowToastShort(getString(R.string.toast_msg_camera_disconnect));
        } catch (Exception e) {
            e.printStackTrace();
            Common.ShowToastShort(getString(R.string.toast_msg_camera_disconnect));
        } finally {
            try {
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void onResume() {

        super.onResume();
        if(mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        }else {
            Log.d("nu","no null");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                Intent intent = new Intent(FragmentCamera.this,PictureActivity.class);
//                startActivity(intent);
            }
        };
        return picture;
    }

    private final Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = null;
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                opt.inMutable = true;
                opt.inJustDecodeBounds = false;
                opt.inDither = false;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                opt.inPreferredConfig = Bitmap.Config.RGB_565;

                Matrix matrix = new Matrix();
                //int witdh = 800, height = 600;


                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);

                matrix.setScale(-1, -1);
                matrix.postRotate(270);
                bitmap = getResizedBitmap(bitmap, 960, 540, matrix);


                if (!SaveImageToDisk(bitmap)) {
//                    Common.ShowToastLong(getString(R.string.toast_msg_err_save_picture_please_try));
                }
            } catch (OutOfMemoryError ex) {
                Common.ShowToastShort(getString(R.string.toast_msg_err_save_picture_please_try));
                //ex.printStackTrace();

            } finally {

                try {
                    mPreview.getmCamera().startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, Matrix matrix) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        //Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, true);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return resizedBitmap;
    }


    // save image to storage
    private boolean SaveImageToDisk(Bitmap bm) {


        // Write to SD Card
        try {
            String fileName = RegisterActivity.PHONENUMBER+String.format("%d.jpg",
                    System.currentTimeMillis());
            File dir;
            File outFile;
            File sdCard = Environment.getExternalStorageDirectory();

            dir = new File(sdCard.getAbsolutePath() + "/DCIM/GoPDU");
            dir.mkdirs();
            outFile = new File(dir, fileName);

            FileOutputStream out = new FileOutputStream(outFile);

            bm.compress(Bitmap.CompressFormat.JPEG, PICTURE_QUALITY, out);
            out.flush();
            out.close();

            image = new ImageAlbum();
            SimpleDateFormat sdf = new SimpleDateFormat(GoPDUApplication.getInstance().getString(R.string.patternDate));

            image.setPath(dir.getPath()+"/"+fileName);

            Bundle bundle = new Bundle();
            bundle.putSerializable("image", image);
            transactFragment(new FragmentViewPicture(), bundle);

            progressDialog.dismiss();
            refreshGallery(outFile);

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bm != null && !bm.isRecycled()) {
                try {
                    bm.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
    // sound on click take a photo
    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            // Log.d(TAG, "onShutter'd");
        }
    };

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onDestroy() {
        if (mPreview != null) {
            try {
                mPreview.stop();
                binding.cPreview.removeView(mPreview);
                mPreview = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (imgPreview != null) {
            try {
                imgPreview.setImageDrawable(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();
    }

    //Focus camera
    public void touchFocusCamera( final Rect touchFocusRect ) {

        //Convert touche coordinate, in width and height to -/+ 1000 range
        final Rect targetFocusRect = new Rect(
                touchFocusRect.left * 2000/mPreview.getWidth() - 1000,
                touchFocusRect.top * 2000/mPreview.getHeight() - 1000,
                touchFocusRect.right * 2000/mPreview.getWidth() - 1000,
                touchFocusRect.bottom * 2000/mPreview.getHeight() - 1000);

        final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
        Camera.Area focusArea = new Camera.Area(targetFocusRect, 1000);
        focusList.add(focusArea);

        Camera.Parameters para = mCamera.getParameters();
        List<String> supportedFocusModes = para.getSupportedFocusModes();
        if ( supportedFocusModes != null &&
                supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO) ) {

            try {

                para.setFocusAreas(focusList);
                para.setMeteringAreas(focusList);
                mCamera.setParameters(para);

                mCamera.autoFocus( FragmentCamera.this );

            } catch (Exception e) {
                Log.e("BBB", "handleFocus: " + e.getMessage() );
            }

        }

    }

    //Auto focus
    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if ( success ) {
            camera.cancelAutoFocus();
        }

        float focusDistances[] = new float[3];
        camera.getParameters().getFocusDistances(focusDistances);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode ==1){
            Intent intent = new Intent();
            intent.putExtra("image", image);
            getActivity().setResult(1, intent);
            getActivity().finish();
        }
    }

    //Load fragment form fragment
    public void transactFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
