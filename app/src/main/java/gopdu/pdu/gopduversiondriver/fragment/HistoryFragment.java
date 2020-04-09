package gopdu.pdu.gopduversiondriver.fragment;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.adapter.MainViewpager;
import gopdu.pdu.gopduversiondriver.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);

        init();
        return binding.getRoot();
    }

    private void init() {

        MainViewpager mainViewpager = new MainViewpager(getFragmentManager());
        mainViewpager.addfragment(new Fragment(), "Accept");
        mainViewpager.addfragment(new Fragment(), "Cancel");

        binding.viewPagerHistory.setAdapter(mainViewpager);
        binding.tabHistory.setupWithViewPager(binding.viewPagerHistory);
        binding.tabHistory.getTabAt(0);
        binding.tabHistory.getTabAt(1);


    }
}
