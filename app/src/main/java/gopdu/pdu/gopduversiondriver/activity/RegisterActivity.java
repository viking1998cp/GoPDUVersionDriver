package gopdu.pdu.gopduversiondriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.HashMap;
import java.util.Map;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityRegisterBinding;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.object.ServerResponse;
import gopdu.pdu.gopduversiondriver.presenter.PresenterRegister;
import gopdu.pdu.gopduversiondriver.service.APIService;
import gopdu.pdu.gopduversiondriver.service.DataService;
import gopdu.pdu.gopduversiondriver.view.ViewRegisterListener;
import gopdu.pdu.gopduversiondriver.viewmodel.CheckExitsViewModel;

/**
 * Created by manh thắng 98.
 */

public class RegisterActivity extends BaseActivity implements ViewRegisterListener {

    private ActivityRegisterBinding binding;
    private DataService dataService;
    private Driver driver;
    public static String PHONENUMBER;
    private CheckExitsViewModel checkExitsViewModel;
    private PresenterRegister presenterRegister;
    private String name , phone, birthdate, gender, licenseplates;
    private ProgressDialog progress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);

        init();
        ActionToolbar();
        setUpOnClick();
    }




    private void init() {
        progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage(getString(R.string.waitingProcess));
        progress.setCancelable(false);
        checkExitsViewModel = ViewModelProviders.of(this).get(CheckExitsViewModel.class);
        binding.iclRegister.etName.setFocusable(true);
        Common.softInput(RegisterActivity.this, true);
        dataService = APIService.getService();
        driver = new Driver();
        checkExitsViewModel = ViewModelProviders.of(this).get(CheckExitsViewModel.class);
        presenterRegister = new PresenterRegister(this);
    }

    private void setUpOnClick() {
        binding.iclRegister.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        pushData();
        presenterRegister.checknull(name, phone, birthdate, gender, licenseplates);

    }

    private void pushData() {
        name = binding.iclRegister.etName.getText().toString();
        birthdate = binding.iclRegister.etBirthDate.getText().toString();
        phone = binding.iclRegister.etNumberPhone.getText().toString();
        licenseplates = binding.iclRegister.etLicensePlate.getText().toString();
        gender = "";
        switch (binding.iclRegister.rdGroupGender.getCheckedRadioButtonId()) {
            case R.id.rdMale:
                gender = "Nam";
                break;
            case R.id.rdFemale:
                gender = "Nữ";
                break;
        }
    }


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

    @Override
    public void onExits() {
        progress.dismiss();
        Common.ShowToastShort(getString(R.string.exitsAccount));
    }

    @Override
    public void onNotExits() {
        progress.dismiss();
        Intent intent = new Intent(RegisterActivity.this, LicenseDriverImageActivity.class);
        Driver driver = new Driver();
        driver.setName(name);
        driver.setBirthdate(birthdate);
        driver.setGender(gender);
        driver.setNumberphone(phone);
        driver.setLicenseplates(licenseplates);
        intent.putExtra(getString(R.string.objectDriver), driver);
        PHONENUMBER = phone;
        startActivity(intent);
        Animatoo.animateSlideLeft(RegisterActivity.this);
    }

    @Override
    public void onEmpty() {
        Common.ShowToastShort(getString(R.string.errorEmpty));
    }

    @Override
    public void onNotEmpty() {
        progress.show();
        final String phone = binding.iclRegister.etNumberPhone.getText().toString();
        Map<String, String> param = new HashMap<>();
        param.put("driverOrCustomer", "Driver");
        param.put("numberphone", Common.formatPhoneNumber(phone));
        checkExitsViewModel.CheckExitsAccount(param).observe(this, new Observer<ServerResponse>() {
            @Override
            public void onChanged(ServerResponse serverResponse) {
                Log.d("BBB", "onChanged: "+serverResponse.getMessage());
                presenterRegister.checkExits(serverResponse.getSuccess());
            }
        });

    }
}
