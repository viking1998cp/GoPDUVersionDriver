package gopdu.pdu.gopduversiondriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.jorgecastillo.State;
import com.github.jorgecastillo.listener.OnStateChangeListener;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.MainActivity;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityRegisterBinding;
import gopdu.pdu.gopduversiondriver.databinding.ActivityRegisterResultBinding;
import gopdu.pdu.gopduversiondriver.presenter.PresenterRegisterResultActivity;
import gopdu.pdu.gopduversiondriver.view.ViewRegisterResultListener;

public class RegisterResultActivity extends AppCompatActivity implements ViewRegisterResultListener {

    private ActivityRegisterResultBinding binding;
    private String result;
    private PresenterRegisterResultActivity presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_result);
        init();
        setUpOnClick();
    }

    private void setUpOnClick() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        if(getIntent().getStringExtra(getString(R.string.resultRegister)) != null){
            result = getIntent().getStringExtra(getString(R.string.resultRegister));
        }
        presenter = new PresenterRegisterResultActivity(this);
        presenter.reciverShowResult(result);

    }

    @Override
    public void showFaild() {
        binding.avgResult.setGlyphStrings(getResources().getStringArray(R.array.arraySvgFaild));
        binding.avgResult.setFillColors(getResources().getIntArray(R.array.arrayColorFaild));
        binding.avgResult.rebuildGlyphData();
        binding.tvResult.setText(getString(R.string.registerFaild));
        binding.tvNotification.setText(getString(R.string.notificationRegisterFaild));
        binding.avgResult.start();
    }

    @Override
    public void showSuccess() {
        binding.avgResult.setGlyphStrings(getResources().getStringArray(R.array.arraySvgSuccess));
        binding.avgResult.setFillColors(getResources().getIntArray(R.array.arrayColorSuccess));
        binding.avgResult.rebuildGlyphData();
        binding.tvResult.setText(getString(R.string.registerSuccess));
        binding.avgResult.start();
    }
}
