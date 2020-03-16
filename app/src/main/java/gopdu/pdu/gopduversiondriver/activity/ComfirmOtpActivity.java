package gopdu.pdu.gopduversiondriver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityComfirmOtpBinding;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.presenter.PresenterComfirmOtp;
import gopdu.pdu.gopduversiondriver.view.ViewComfirmOtpListener;
import gopdu.pdu.gopduversiondriver.viewmodel.CheckIdCurentViewModel;

public class ComfirmOtpActivity extends BaseActivity implements ViewComfirmOtpListener{

    private ActivityComfirmOtpBinding binding;
    private String numberPhone;
    private PresenterComfirmOtp presenter;
    private FirebaseAuth mAuth;
    private String VerifyCationId;
    private ProgressDialog progress;
    private CheckIdCurentViewModel checkIdCurentModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_comfirm_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comfirm_otp);
        init();
        setUpView();
        setUpOnClick();
        ActionToolbar();
    }

    private void setUpOnClick() {
        binding.iclComfirm.tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.iclComfirm.tvTimer.equals(getString(R.string.send_again_otp))){
                    presenter.sendOtp(numberPhone);
                    presenter.reciverSetUpView();

                }
            }
        });

        binding.iclComfirm.btnAcvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reciverCheckNullOtp(binding.iclComfirm.otpComfirm.getText().toString());
            }
        });
    }

    private void setUpView() {
        presenter.reciverSetUpView();
    }

    private void init() {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.waitingProcess));
        checkIdCurentModel = ViewModelProviders.of(this).get(CheckIdCurentViewModel.class);
        presenter = new PresenterComfirmOtp(this);
        if(getIntent().getStringExtra(getString(R.string.paramNumberPhone)) != null){
            numberPhone = getIntent().getStringExtra(getString(R.string.paramNumberPhone));
            binding.iclComfirm.tvHelper.setText(getString(R.string.helperActivityOTP, numberPhone));
            presenter.sendOtp(numberPhone);
        }

        mAuth =FirebaseAuth.getInstance();
    }


    //Send otp
    @Override
    public void sendOtp(String numberPhone) {
        SendVerifyCode(numberPhone);
    }


    //Start time opt
    @Override
    public void setUpTimer(long millisUntilFinished) {
        binding.iclComfirm.tvTimer.setText(getString(R.string.timerotp ,(int) millisUntilFinished/1000));
    }

    @Override
    public void setUpFinishTimer() {
        binding.iclComfirm.tvTimer.setText(getString(R.string.send_again_otp));
    }


    //Check null otp
    @Override
    public void notNullOtp(String otp) {
        progress.show();
        verifyCode(otp);
    }

    @Override
    public void nullOtp() {
        Common.ShowToastShort(getString(R.string.emptyOtp));
    }

    @Override
    public void faildLogin() {
        progress.dismiss();
        Common.ShowToastShort(getString(R.string.errorOtp));
    }

    @Override
    public void successLogin() {

        Map<String , String> param  = new HashMap<>();
        param.put(getString(R.string.paramNumberPhone), Common.formatPhoneNumber(numberPhone));
        param.put(getString(R.string.paramID), mAuth.getUid());
        checkIdCurentModel.CheckIdCurent(param).observe(this, new Observer<ServerResponse>() {
            @Override
            public void onChanged(ServerResponse serverResponse) {
                presenter.reciverCheckChangeIdSuccess(serverResponse);
            }
        });


    }

    @Override
    public void faildChange() {
        progress.dismiss();
        Common.ShowToastShort(getString(R.string.erroClient));
    }

    @Override
    public void successChange() {
        progress.dismiss();
        Intent intent = new Intent(ComfirmOtpActivity.this, DriverUseMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerifyCationId,code); //Kiểm tra mã code được
        SigInWithCredential(credential);
    }
    private void SigInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                presenter.reciverCheckSuccessLogin(task);
            }
        });
    }

    private void SendVerifyCode(String PhoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneNumber
                ,60
                , TimeUnit.SECONDS
                , TaskExecutors.MAIN_THREAD
                ,mcallback);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerifyCationId =s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
    };
    private void ActionToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
