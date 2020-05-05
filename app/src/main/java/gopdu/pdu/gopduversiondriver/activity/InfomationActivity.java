package gopdu.pdu.gopduversiondriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import gopdu.pdu.gopduversiondriver.Database.DataDriver;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityInfomationBinding;
import gopdu.pdu.gopduversiondriver.object.Driver;

public class InfomationActivity extends AppCompatActivity {

    private ActivityInfomationBinding binding;
    private DataDriver dataDriver;
    private Driver driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_infomation);

        init();
        setUpView();
        setUpOnClick();
    }

    private void setUpOnClick() {
        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        dataDriver = DataDriver.getInMemoryDatabase(getApplicationContext());
        driver = dataDriver.driverDao().findAllEmploySync().get(0);

    }

    private void setUpView() {

        binding.etName.setEnabled(false);
        binding.etPhone.setEnabled(false);
        binding.etLicencePlate.setEnabled(false);
        binding.etPhone.setText(driver.getNumberphone());
        binding.etName.setText(driver.getName());
        binding.etLicencePlate.setText(driver.getLicenseplates());
        Glide.with(getApplicationContext())
                .load(driver.getImvDriverface())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imvDriverFace);
    }
}
