package gopdu.pdu.gopduversiondriver;

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
import android.view.Window;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import gopdu.pdu.gopduversiondriver.activity.ComfirmOtpActivity;
import gopdu.pdu.gopduversiondriver.activity.DriverUseMainActivity;
import gopdu.pdu.gopduversiondriver.activity.RegisterActivity;
import gopdu.pdu.gopduversiondriver.databinding.ActivityMainBinding;
import gopdu.pdu.gopduversiondriver.dialog.CheckDialog;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.presenter.PresenterMainActivity;
import gopdu.pdu.gopduversiondriver.view.ViewMainActivityListener;
import gopdu.pdu.gopduversiondriver.viewmodel.CheckExitsViewModel;

public class MainActivity extends BaseActivity implements ViewMainActivityListener {

    private ActivityMainBinding binding;
    private PresenterMainActivity presenter;
    private CheckExitsViewModel checkExitsViewModel;
    private ProgressDialog progress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mStateListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        init();
        setUpOnClick();

    }

    CheckDialog checkDialog;
    private void init() {
        presenter = new PresenterMainActivity(this);
        checkExitsViewModel = ViewModelProviders.of(this).get(CheckExitsViewModel.class);
        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage(getString(R.string.waitingProcess));
        progress.setCanceledOnTouchOutside(false);
        mAuth = FirebaseAuth.getInstance();
        mStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                presenter.reciverCheckLogged(firebaseAuth);
            }
        };
    }

    private void setUpOnClick() {

        binding.iclMain.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                Animatoo.animateZoom(MainActivity.this);
            }
        });

        binding.iclMain.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.iclMain.etPhone.getText().toString();
                presenter.reciverNullPhoneNumber(phone);
            }
        });
    }

    @Override
    public void nullPhoneNumber() {

        Common.ShowToastShort(getString(R.string.emptyNumberPhone));
    }




    @Override
    public void notNullPhoneNumber(Map<String, String> param) {
        progress.show();
        checkExitsViewModel.CheckExitsAccount(param).observe(this, new Observer<ServerResponse>() {
            @Override
            public void onChanged(ServerResponse serverResponse) {
                if(serverResponse != null){
                    presenter.reciverCheckExits(serverResponse);
                }else {
                    progress.dismiss();
                    Common.ShowToastShort(getString(R.string.checkConnect));
                }
            }
        });
    }

    @Override
    public void notAvalidAccount(String message) {
        progress.dismiss();
        Common.ShowToastShort(message);
    }

    @Override
    public void avalidAccount() {
        progress.dismiss();
        Intent intent = new Intent(MainActivity.this, ComfirmOtpActivity.class);
        intent.putExtra(getString(R.string.paramNumberPhone),Common.formatPhoneNumber(binding.iclMain.etPhone.getText().toString()));
        startActivity(intent);
    }

    @Override
    public void logged() {
        Intent intent = new Intent(MainActivity.this, DriverUseMainActivity.class);
        startActivity(intent);
        progress.dismiss();
        this.finish();
    }

    @Override
    public void unLogged() {
        progress.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mStateListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
