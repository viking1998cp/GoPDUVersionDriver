package gopdu.pdu.gopduversiondriver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.activity.InfomationActivity;
import gopdu.pdu.gopduversiondriver.databinding.FragmentSettingBinding;
import gopdu.pdu.gopduversiondriver.viewmodel.InsertHistoryViewModel;

public class SettingFragment extends Fragment {

    FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);

        setUpOnClick();
        return binding.getRoot();
    }

    private void setUpOnClick() {
        binding.lnInfomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfomationActivity.class);
                startActivity(intent);
            }
        });
    }
}
