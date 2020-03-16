package gopdu.pdu.gopduversiondriver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import gopdu.pdu.gopduversiondriver.R;
import gopdu.pdu.gopduversiondriver.databinding.DialogCheckBinding;

public class CheckDialog extends Dialog {
    //Icon loading equasl logo adidi
    private DialogCheckBinding binding;

    public CheckDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_check,
                null,
                false);
        setContentView(binding.getRoot());
    }
    public void messengerDialog(String messenger){
        binding.tvMessenger.setText(messenger);
    }
    public void showing(){
        if(this.isShowing()){
            this.dismiss();
        }
        this.isShowing();
    }

}
