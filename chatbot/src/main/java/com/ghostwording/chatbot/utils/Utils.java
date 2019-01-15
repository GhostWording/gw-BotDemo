package com.ghostwording.chatbot.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.ghostwording.chatbot.ChatBotApplication;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.dialog.ShareDialog;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.model.intentions.Intention;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.textimagepreviews.TranslationActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {

    public static final int ENGLISH = 0;
    public static final int FRENCH = 1;
    public static final int SPANISH = 2;

    public static final String GIF_FILENAME = "test.gif";
    public static final String LOVE_RELATION_TAG = "47B7E9";

    public static final String WHATS_APP_PACKAGE = "com.whatsapp";
    public static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    public static final String VIBER_PACKAGE = "com.viber.voip";
    public static final String INSTAGRAMM_PACKAGE = "com.instagram.android";
    public static final String SNAPCHAT_PACKAGE = "com.snapchat.android";
    public static final String TWITTER_PACKAGE = "com.twitter.android";
    public static final String FACEBOOK_MESSNEGER_PACKAGE = "com.facebook.orca";

    public static final SimpleDateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static String getFilenameFromUri(String imageUri) {
        try {
            return imageUri.substring(imageUri.lastIndexOf('/') + 1);
        } catch (Exception ex) {
            Logger.e("Wrong image uri. " + ex.toString());
        }
        return "";
    }

    public static void shareOnFacebook(Activity activity) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=https://play.google.com/store/apps/details?id=com.wavemining.hugs")));
    }

    public static void copyTextToClipBoard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = android.content.ClipData.newPlainText(context.getString(R.string.app_name), text);
        clipboard.setPrimaryClip(clip);
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static String getLocalizedLabel(BotSequence.Label label) {
        if (label == null) {
            return null;
        }
        int selectedLanguage = LocaleManager.getSelectedLanguage(ChatBotApplication.instance());
        if (selectedLanguage == 0) {
            return label.getEn();
        }
        if (selectedLanguage == 1) {
            return label.getFr();
        }
        if (selectedLanguage == 2) {
            return label.getEs();
        }
        return null;
    }

    public static String getFullImagePrefix() {
        return "http://az767698.vo.msecnd.net/canonical/";
    }

    public static void shareSticker(AppCompatActivity activity, String imageUrl, Quote quote) {
        shareSticker(activity, imageUrl, null, quote);
    }

    public static void shareSticker(AppCompatActivity activity, String imageUrl, String imageName, Quote quote) {
        if (quote != null) {
            Intent intent = new Intent(activity, TranslationActivity.class);
            intent.putExtra(TranslationActivity.QUOTE, quote);
            intent.putExtra(TranslationActivity.IMAGE_URL, imageUrl);
            intent.putExtra(TranslationActivity.IMAGE_NAME, imageName);
            activity.startActivity(intent);
        } else {
            ShareDialog.show(activity, imageUrl, quote, false);
        }
    }

    public static String getImagePath(Quote quote) {
        if (quote != null && quote.getIntentionId() != null) {
            for (Intention item : AppConfiguration.getIntentions()) {
                if (quote.getIntentionId().equals(item.getIntentionId())) {
                    return item.getImagePath();
                }
            }
        }
        return null;
    }

    public static String getLanguageString(int language) {
        String result = "fr";
        switch (language) {
            case ENGLISH:
                result = "en";
                break;
            case FRENCH:
                result = "fr";
                break;
            case SPANISH:
                result = "es";
                break;
        }
        return result;
    }

    public static int getCurrentLocaleLanguageIndex() {
        switch (Resources.getSystem().getConfiguration().locale.getLanguage()) {
            case "en":
                return ENGLISH;
            case "fr":
                return FRENCH;
            case "es":
                return SPANISH;
        }
        return -1;
    }

    public static void rateManually(Activity activity) {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.STORE_RATING);
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (ActivityNotFoundException activityNotFoundException1) {
            Logger.e("Market Intent not found");
        }
    }

    public static String getImagePrefix() {
        return "http://az767698.vo.msecnd.net/alternate/300px/";
    }

    public static void setLanguage(Context context, int language) {
        setLanguage(context, language, false);
    }

    public static void setLanguage(Context context, int language, boolean fromScreen) {
        String languageToLoad = getLanguageString(language);
        LocaleManager.setNewLocale(context, languageToLoad, language);
        if (fromScreen) {
            ApiClient.clearData();
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.SET_LANGUAGE, languageToLoad);
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.LANGUAGE, languageToLoad);
        }
    }

    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

}
