package gopdu.pdu.gopduversiondriver.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.FragmentPictrureBinding;
import gopdu.pdu.gopduversiondriver.object.ImageAlbum;

/**
 * Created by manh thắng 98.
 */
public class FragmentViewPicture extends Fragment {

    private FragmentPictrureBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private String REQUEST_CODE ;

    private ImageAlbum image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pictrure, container, false);

        init();

        initView();
        setUpOnClick();

        return binding.getRoot();
    }

    private void init() {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            image = (ImageAlbum) bundle.getSerializable("image");
        }

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        REQUEST_CODE = (String) getActivity().getIntent().getStringExtra(GoPDUApplication.getInstance().getString(R.string.request_imv));
        Log.d("BBB", "init: "+REQUEST_CODE);

    }

    private void setUpOnClick() {

        binding.imvAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        binding.imvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.imv), image);
                if(REQUEST_CODE.equals(getString(R.string.licenseDriverFront))){

                    getActivity().setResult(Common.RQUEST_CODE_LICENSE_FRONT, intent);
                    getActivity().finish();
                }else if(REQUEST_CODE.equals(getString(R.string.licenseDriverBackSide))){

                    getActivity().setResult(Common.RQUEST_CODE_LICENSE_BACK, intent);
                    getActivity().finish();
                }else if(REQUEST_CODE.equals(getString(R.string.identityFront))){

                    getActivity().setResult(Common.RQUEST_CODE_IDENTITY_CARD_FRONT, intent);
                    getActivity().finish();
                }else if(REQUEST_CODE.equals(getString(R.string.identityBackSide))){

                    getActivity().setResult(Common.RQUEST_CODE_IDENTITY_CARD_BACKSIDE, intent);
                    getActivity().finish();
                }else if(REQUEST_CODE.equals(getString(R.string.driverFace))){

                    getActivity().setResult(Common.RQUEST_CODE_DRIVER_FACE, intent);
                    getActivity().finish();
                }else if(REQUEST_CODE.equals(getString(R.string.motorcyclepapersFront))){

                    getActivity().setResult(Common.RQUEST_CODE_MOTORCYCLEPAPER_FRONT, intent);
                    getActivity().finish();
                }else if(REQUEST_CODE.equals(getString(R.string.motorcyclepapersBackSide))){

                    getActivity().setResult(Common.RQUEST_CODE_MOTORCYCLEPAPER_BACKSIDE, intent);
                    getActivity().finish();
                }
            }
        });

        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    private void initView() {
        Bitmap bitmap = Common.returnBitmapFromStorage(image.getPath());
        if(bitmap != null){
            binding.imvImage.setImageBitmap(bitmap);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            binding.imvImage.setScaleX(mScaleFactor);
            binding.imvImage.setScaleY(mScaleFactor);
            return true;
        }
    }
}
