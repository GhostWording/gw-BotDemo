package com.ghostwording.chatbot.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.databinding.DialogSendChooserBinding;
import com.ghostwording.chatbot.databinding.ItemSideMenuPromotionBinding;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsShare;

public class ShareDialog extends BaseDialog {

    private String selectedImageUri;
    private Quote selectedQuote;
    private boolean isBubble = false;

    public static void show(FragmentActivity activity, String selectedImageUri, Quote selectedQuote, boolean isBubble) {
        if (selectedQuote != null) {
            AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.SEND_MENU_OPENED, selectedQuote.getTextId(), Utils.getFilenameFromUri(selectedImageUri));
        }
        try {
            ShareDialog sendDialog = new ShareDialog();
            sendDialog.selectedImageUri = selectedImageUri;
            sendDialog.selectedQuote = selectedQuote;
            sendDialog.isBubble = isBubble;
            sendDialog.show(activity.getSupportFragmentManager(), ShareDialog.class.getSimpleName());
        } catch (Exception ex) {
            Logger.e("State lost on some devices : " + ex.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_send_chooser, null);
        DialogSendChooserBinding binding = DataBindingUtil.bind(v);
        new SendItemsView(binding.container);
        return v;
    }

    public class SendItemsView {

        public SendItemsView(LinearLayout container) {
            final Context context = container.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            final String[] shareLabels = context.getResources().getStringArray(R.array.share_custom_dialog_text);
            TypedArray shareIcons = context.getResources().obtainTypedArray(R.array.share_custom_dialog_items);

            for (int i = 0; i < shareLabels.length; i++) {
                final String action = shareLabels[i];

                if (!filterActionByContent(action)) {
                    continue;
                }

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
                if (action.equals(getString(R.string.share_dialog_messenger)) && !Utils.isPackageInstalled(getActivity(), Utils.FACEBOOK_MESSNEGER_PACKAGE)) {
                    continue;
                }
                if (action.equals(getString(R.string.share_dialog_facebook)) && !Utils.isPackageInstalled(getActivity(), Utils.FACEBOOK_PACKAGE)) {
                    continue;
                }

                View itemView = layoutInflater.inflate(R.layout.item_side_menu_promotion, null);
                ItemSideMenuPromotionBinding binding = DataBindingUtil.bind(itemView);
                binding.promotionIcon.setImageResource(shareIcons.getResourceId(i, -1));
                binding.promotionTitle.setText(action);
                binding.container.setOnClickListener(v -> onShareAction(action));
                container.addView(itemView);
            }

            shareIcons.recycle();
        }

        private boolean filterActionByContent(String action) {
            final int MAX_TWITTER_MESSAGE_LENGTH = 140;
            if (selectedQuote == null) {
                if (action.equals(getString(R.string.share_dialog_separate))) {
                    return false;
                }
            } else {
                if (action.equals(getString(R.string.share_dialog_snapchat))) {
                    return false;
                }
                if (action.equals(getString(R.string.share_dialog_twitter)) && selectedQuote.getContent().length() > MAX_TWITTER_MESSAGE_LENGTH) {
                    return false;
                }
            }

            return true;
        }

        private void onShareAction(String action) {
            if (action.equals(getString(R.string.share_dialog_messenger))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_MESSENGER, selectedQuote, selectedImageUri, AnalyticsHelper.Parameters.TEXT_PLUS_IMAGE);
                UtilsShare.shareByPackageName(getActivity(), Utils.FACEBOOK_MESSNEGER_PACKAGE, selectedImageUri, selectedQuote, true, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_facebook))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_FACEBOOK_WALL, selectedQuote, selectedImageUri, AnalyticsHelper.Parameters.TEXT_PLUS_IMAGE);
                UtilsShare.shareByPackageName(getActivity(), Utils.FACEBOOK_PACKAGE, selectedImageUri, selectedQuote, true, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_as_postcard))) {
                UtilsShare.shareViaIntent(getActivity(), selectedImageUri, selectedQuote, true, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_separate))) {
                UtilsShare.shareViaIntent(getActivity(), selectedImageUri, selectedQuote, false, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_whatsapp))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_WHATS_APP, selectedQuote, selectedImageUri, AnalyticsHelper.Parameters.TEXT_PLUS_IMAGE);
                UtilsShare.shareByPackageName(getActivity(), Utils.WHATS_APP_PACKAGE, selectedImageUri, selectedQuote, false, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_viber))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_VIBER, selectedQuote, selectedImageUri, null);
                UtilsShare.shareByPackageName(getActivity(), Utils.VIBER_PACKAGE, selectedImageUri, selectedQuote, true, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_snapchat))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_SNAPCHAT, selectedQuote, selectedImageUri, null);
                UtilsShare.shareByPackageName(getActivity(), Utils.SNAPCHAT_PACKAGE, selectedImageUri, selectedQuote, true, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_instagram))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_INSTAGRAMM, selectedQuote, selectedImageUri, null);
                UtilsShare.shareByPackageName(getActivity(), Utils.INSTAGRAMM_PACKAGE, selectedImageUri, selectedQuote, true, isBubble);
            }
            if (action.equals(getString(R.string.share_dialog_twitter))) {
                AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_TWITTER, selectedQuote, selectedImageUri, null);
                UtilsShare.shareByPackageName(getActivity(), Utils.TWITTER_PACKAGE, selectedImageUri, selectedQuote, false, isBubble);
            }
            dismiss();
        }
    }

}