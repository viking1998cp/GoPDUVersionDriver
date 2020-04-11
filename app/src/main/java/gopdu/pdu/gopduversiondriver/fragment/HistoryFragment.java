package gopdu.pdu.gopduversiondriver.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import gopdu.pdu.gopduversiondriver.Common;
import gopdu.pdu.gopduversiondriver.GoPDUApplication;
import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.adapter.ItemHistoryAdapter;
import gopdu.pdu.gopduversiondriver.adapter.MainViewpager;
import gopdu.pdu.gopduversiondriver.databinding.FragmentHistoryBinding;
import gopdu.pdu.gopduversiondriver.network.HistoryResponse;
import gopdu.pdu.gopduversiondriver.object.History;
import gopdu.pdu.gopduversiondriver.presenter.PresenterHistoryFragment;
import gopdu.pdu.gopduversiondriver.view.ViewHistoryFragmentListener;
import gopdu.pdu.gopduversiondriver.viewmodel.GetHistoryViewModel;

public class HistoryFragment extends Fragment implements ViewHistoryFragmentListener {

    private FragmentHistoryBinding binding;
    private String userid;
    private ArrayList<History> histories;
    private ItemHistoryAdapter historyAdapter;
    private boolean isLoading = false;
    private boolean limitData = false;

    private PresenterHistoryFragment presenter;
    private ProgressDialog progressDialog;

    private String  state;

    private int page =1;

    private GetHistoryViewModel getHistoryModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);

        init();
        GetData(page);
        setUpOnClick();
        initScrollListener();
        return binding.getRoot();
    }

    private void setUpOnClick() {

        binding.tabHistory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isLoading =false;
                limitData =false;
                stopHandler();
                progressDialog.show();
                presenter.reciverStateHistoryChange(tab.getPosition());
                histories.clear();
                page=1;
                GetData(page);
                historyAdapter.notifyDataSetChanged();
                binding.rclHistory.setAdapter(historyAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        historyAdapter.setOnItemClickedListener(new ItemHistoryAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int postion, View v) {
                
            }
        });
    }

    private void GetData(int page) {
        progressDialog.show();
        HashMap param = new HashMap<>();
        param.put(getString(R.string.paramID), userid);
        param.put(getString(R.string.paramPage), page);
        param.put(getString(R.string.paramState), state);

        getHistoryModel.getHistoryResponseLiveData(param).observe(this, new Observer<HistoryResponse>() {
            @Override
            public void onChanged(HistoryResponse historyResponse) {
                presenter.reciverGetHistory(historyResponse);
            }
        });;
    }


    private void init() {
        state = GoPDUApplication.getInstance().getString(R.string.param_StateSuccess);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(GoPDUApplication.getInstance().getString(R.string.waitingProcess));
        progressDialog.setCancelable(false);

        presenter = new PresenterHistoryFragment(this);
        getHistoryModel = ViewModelProviders.of(this).get(GetHistoryViewModel.class);
        histories = new ArrayList<>();
        historyAdapter = new ItemHistoryAdapter(histories);
        binding.rclHistory.setAdapter(historyAdapter);

        MainViewpager mainViewpager = new MainViewpager(getFragmentManager());
        mainViewpager.addfragment(new Fragment(), "Accept");
        mainViewpager.addfragment(new Fragment(), "Cancel");

        binding.viewPagerHistory.setAdapter(mainViewpager);
        binding.tabHistory.setupWithViewPager(binding.viewPagerHistory);
        binding.tabHistory.getTabAt(0);
        binding.tabHistory.getTabAt(1);
        userid = FirebaseAuth.getInstance().getUid();

    }

    private void initScrollListener() {
        binding.rclHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && !limitData  ) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == histories.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    Runnable my_runnable = new Runnable() {
        @Override
        public void run() {
            histories.remove(histories.size() - 1);
            int scrollPosition = histories.size();
            historyAdapter.notifyItemRemoved(scrollPosition);

            GetData(++page);

            historyAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    };

    public Handler handler = new Handler(); // use 'new Handler(Looper.getMainLooper());' if you want this handler to control something in the UI
    // to start the handler
    public void startHandler() {
        handler.postDelayed(my_runnable, 2000);
    }

    // to stop the handler
    public void stopHandler() {
        handler.removeCallbacks(my_runnable);
    }

    // to reset the handler
    public void restart() {
        handler.removeCallbacks(my_runnable);
        handler.postDelayed(my_runnable, 2000);
    }

    private void loadMore() {
        histories.add(null);
        historyAdapter.notifyItemInserted(histories.size() - 1);

        startHandler();
    }



    @Override
    public void getHistorySuccess(ArrayList<History> data) {
        histories.addAll(data);
        historyAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void getHistoryFaild() {
        progressDialog.dismiss();
        Common.ShowToastShort(getString(R.string.checkConnect));
    }

    @Override
    public void getHistoryStateSuccess(String state) {
        this.state = state;
    }

    @Override
    public void getHistoryStateCancel(String state) {
        this.state = state;
    }

    @Override
    public void reciverAllDataHistory() {
        limitData = true;
        Common.ShowToastShort(GoPDUApplication.getInstance().getString(R.string.reciverAllData));
        progressDialog.dismiss();
    }
}
