package gopdu.pdu.gopduversiondriver.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gopdu.pdu.gopduversiondriver.BaseActivity;
import gopdu.pdu.gopduversiondriver.IOnBackPressed;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.ActivityDriverUseMainBinding;
import gopdu.pdu.gopduversiondriver.fragment.DriverMapsFragment;
import gopdu.pdu.gopduversiondriver.fragment.HistoryFragment;

public class DriverUseMainActivity extends BaseActivity {

    private ActivityDriverUseMainBinding binding;
    private int back;
    private DriverMapsFragment driverMapsFragment;
    private HistoryFragment historyFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_use_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_use_main);
        init();
        setUpMenuOnClick();
    }
    private void init() {
        driverMapsFragment = new DriverMapsFragment();
        historyFragment = new HistoryFragment();
        loadFragment(historyFragment, R.id.navigation_work);
    }

    private void setUpMenuOnClick() {
        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_work:
                        if(back != 1){
                            back =1;
                            loadFragment(driverMapsFragment, R.id.navigation_work);
                        }
                        return true;
                    case R.id.navigation_history:
                        if(back != 2){
                            loadFragment(historyFragment, R.id.navigation_history);
                            back =2;
                        }

                        return true;
                    case R.id.navigation_profile:
                        if(back != 3){
//                            fragment = new AboutFragment();
//                            loadFragment(fragment);
                            back =3;
                        }
                        return true;
                }

                return false;
            }
        });
    }


    public void loadFragment(Fragment fragment, int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.addToBackStack(id + "stack_item");
            fragmentTransaction.replace(R.id.frame_container, fragment);

        }
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if(back==1){
            if (!(driverMapsFragment instanceof IOnBackPressed) || !((IOnBackPressed) driverMapsFragment).onBackPressed()) {
                super.onBackPressed();
            }
        }
    }
}
