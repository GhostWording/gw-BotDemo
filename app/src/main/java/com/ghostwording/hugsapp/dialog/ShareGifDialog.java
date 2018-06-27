package com.ghostwording.hugsapp.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.DialogSendChooserBinding;
import com.ghostwording.hugsapp.databinding.ItemSendBinding;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.utils.UtilsShare;

import java.io.File;

public class ShareGifDialog extends BaseDialog {

    private String shareFileUrl;

    public static void show(FragmentActivity activity, String imageUrl) {
        try {
            ShareGifDialog sendGifDialog = new ShareGifDialog();
            imageUrl = imageUrl.replace("file://", "");
            Uri shareFileUri = FileProvider.getUriForFile(activity,
                    "com.wavemining.kittens.fileprovider",
                    new File(imageUrl));
            sendGifDialog.shareFileUrl = shareFileUri.toString();
            sendGifDialog.show(activity.getSupportFragmentManager(), ShareGifDialog.class.getSimpleName());
        } catch (Exception ex) {
            Logger.e("State lost on some devices : " + ex.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_send_chooser, null);
        DialogSendChooserBinding binding = DataBindingUtil.bind(v);
        initSendItems(binding.container);
        return v;
    }

    private void initSendItems(LinearLayout container) {
        final Context context = container.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        final String[] shareLabels = context.getResources().getStringArray(R.array.share_gif_dialog_text);
        TypedArray shareIcons = context.getResources().obtainTypedArray(R.array.share_gif_dialog_items);

        for (int i = 0; i < shareLabels.length; i++) {
            final String action = shareLabels[i];

            if (action.equals(getString(R.string.share_dialog_whatsapp)) && !Utils.isPackageInstalled(getActivity(), Utils.WHATS_APP_PACKAGE)) {
                continue;
            }
            if (action.equals(getString(R.string.share_dialog_viber)) && !Utils.isPackageInstalled(getActivity(), Utils.VIBER_PACKAGE)) {
                continue;
            }
            if (action.equals(getString(R.string.share_dialog_snapchat)) && !Utils.isPackageInstalled(getActivity(), Utils.SNAPCHAT_PACKAGE)) {
                continue;
            }
            if (action.equals(getString(R.string.share_dialog_instagram)) && !Utils.isPackageInstalled(getActivity(), Utils.INSTAGRAMM_PACKAGE)) {
                continue;
            }
            if (action.equals(getString(R.string.share_dialog_twitter)) && !Utils.isPackageInstalled(getActivity(), Utils.TWITTER_PACKAGE)) {
                continue;
            }
            if (action.equals(getString(R.string.share_dialog_facebook)) && !Utils.isPackageInstalled(getActivity(), Utils.FACEBOOK_PACKAGE)) {
                continue;
            }
            if (action.equals(getString(R.string.share_dialog_messenger)) && !Utils.isPackageInstalled(getActivity(), Utils.FACEBOOK_MESSNEGER_PACKAGE)) {
                continue;
            }

            View itemView = layoutInflater.inflate(R.layout.item_send, null);
            ItemSendBinding binding = DataBindingUtil.bind(itemView);
            binding.promotionIcon.setImageResource(shareIcons.getResourceId(i, -1));
            binding.promotionTitle.setText(action);
            binding.container.setOnClickListener(v -> onShareAction(action));
            container.addView(itemView);
        }

        shareIcons.recycle();
    }

    private void onShareAction(String action) {
        Uri shareFileUri = Uri.parse(shareFileUrl);
        if (action.equals(getString(R.string.share_dialog_messenger))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.FACEBOOK_MESSNEGER_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_dialog_facebook))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.FACEBOOK_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_dialog_whatsapp))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.WHATS_APP_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_dialog_viber))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.VIBER_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_dialog_snapchat))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.SNAPCHAT_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_dialog_instagram))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.INSTAGRAMM_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_dialog_twitter))) {
            UtilsShare.shareGifByPackageName(getActivity(), Utils.TWITTER_PACKAGE, shareFileUri);
        }
        if (action.equals(getString(R.string.share_more))) {
            UtilsShare.shareGifImage(getActivity(), shareFileUri);
        }
        dismiss();
    }

}

