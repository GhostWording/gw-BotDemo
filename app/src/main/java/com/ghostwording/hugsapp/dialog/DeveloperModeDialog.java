package com.ghostwording.hugsapp.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.DialogDeveloperModeBinding;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.io.Callback;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.utils.Utils;

import okhttp3.ResponseBody;

public class DeveloperModeDialog extends BaseDialog {

    public static void show(AppCompatActivity activity) {
        DeveloperModeDialog developerModeDialog = new DeveloperModeDialog();
        developerModeDialog.show(activity.getSupportFragmentManager(), DeveloperModeDialog.class.getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_developer_mode, null);
        DialogDeveloperModeBinding binding = DataBindingUtil.bind(v);

        binding.tvDeveloperInfo.setText("Current variation : " + PrefManager.instance().getVariationId() + " AppVersion : " + AppConfiguration.getAppVersionNumber());
        binding.tvDeviceId.setText("DeviceId : " + AppConfiguration.getDeviceId());
        Utils.copyTextToClipBoard(getContext(), AppConfiguration.getDeviceId());

        binding.etBotname.setText(AppConfiguration.getBotName());
        binding.btnSave.setOnClickListener(view -> {
            AppConfiguration.setBotName(binding.etBotname.getText().toString());
            Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
        });
        binding.btnReset.setOnClickListener(view -> {
            PrefManager.instance().setHuggyShown(false);
            PrefManager.instance().setLastSequenceId(binding.etBotname.getText().toString(), null);
            ApiClient.getInstance().testDevService.clearBotHistory(binding.etBotname.getText().toString(), AppConfiguration.getDeviceId()).enqueue(new Callback<ResponseBody>(getActivity()) {
                @Override
                public void onDataLoaded(@Nullable ResponseBody result) {
                    Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.rbVariation1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                PrefManager.instance().setVariationId(1);
            }
        });
        binding.rbVariation2.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                PrefManager.instance().setVariationId(2);
            }
        });
        binding.rbVariation3.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                PrefManager.instance().setVariationId(4);
            }
        });
        binding.btnOk.setOnClickListener(view -> dismiss());

        return v;
    }


}
