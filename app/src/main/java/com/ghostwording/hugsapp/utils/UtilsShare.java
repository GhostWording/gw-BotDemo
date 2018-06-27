package com.ghostwording.hugsapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.model.PictureSource;
import com.ghostwording.hugsapp.model.texts.Quote;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UtilsShare {

    public static final int REQUEST_CODE_SHARE_TO_MESSENGER = 505;

    public static final String RECIPIENT_TYPE = "recipientType";
    public static final String IMAGE_NAME = "imageName";
    public static final String INTENTION_ID = "intentionId";
    public static final String PROTOTYPE_ID = "prototypeId";
    public static final String IMAGE_PATH = "imagePath";
    public static final String TEXT_ID = "textId";
    public static final String TEXT = "text";

    private static final String SHARE_URL_TEMPLATE = "http://www.commentvousdire.com/webapp/%s/area/%s/recipient/%s/intention/%s/text/%s?tagline=Download to Reply&imagePath=%s" + getAppLogoParameter() + getAppStoreLinkParameter();

    private static String getAppStoreLinkParameter() {
        return "";
    }

    private static String getPackageNameParameter(Context context) {
        return "&GooglePlayId=" + context.getPackageName();
    }

    private static String getAppNameParameter(Context context) {
        return "&AppDisplayName=" + context.getString(R.string.app_name);
    }

    private static String getAppLogoParameter() {
        return "&AppLogo=cvd/icons/lbdl/xsmall/logo4small.jpg";
    }

    public static void shareViaIntent(final Activity activity, String selectedImageUri, final Quote quote, final boolean isConcatenateImageWithText, PictureSource pictureSource, boolean isBubble) {
        final ProgressDialog progressDialog = UtilsUI.createProgressDialog(activity, R.string.processing_image, true);
        final String quoteText = quote == null ? null : quote.getContent();
        String additionalParameter = isConcatenateImageWithText ? AnalyticsHelper.Parameters.EMBEDDED_IMAGE : AnalyticsHelper.Parameters.SEPARATE_IMAGE;
        AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_VIA_INTENT, quote, selectedImageUri, pictureSource, additionalParameter);

        PostCardRenderer.requestPrepareSharingImage(activity, selectedImageUri, isConcatenateImageWithText ? quoteText : null, isBubble).subscribe(success -> {
            progressDialog.dismiss();
            if (success) {
                shareImageAndText(activity, isConcatenateImageWithText ? null : quoteText);
            } else {
                UtilsUI.showErrorInSnackBar(activity, activity.getString(R.string.processing_image_error));
            }
        });
    }

    public static void shareByEmail(final Activity activity, String selectedImageUri, @Nullable final Quote selectedQuote) {
        AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_EMAIL, selectedQuote, selectedImageUri, PictureSource.SERVER, null);
        PostCardRenderer.requestPrepareSharingImage(activity, selectedImageUri, null, false).subscribe(success -> {
            if (success) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                if (selectedQuote != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, selectedQuote.getContent());
                }
                intent.putExtra(Intent.EXTRA_STREAM, PostCardRenderer.getShareFileUri(activity));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                }
            } else {
                UtilsUI.showErrorInSnackBar(activity, activity.getString(R.string.processing_image_error));
            }
        });
    }

    public static void shareBySMS(Activity activity, String imageUri, Quote quote) {
        if (quote == null) return;

        if (imageUri.startsWith("file")) {
            imageUri = "";
        }
        AnalyticsHelper.sendShareEvent(AnalyticsHelper.Events.SHARE_SMS, quote, imageUri, PictureSource.SERVER, null);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"));
        intent.putExtra("sms_body", quote.getContent() + "\n\n" + imageUri);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void shareByPackageName(final Activity activity, final String packageName, String imageUri, final Quote quote, final boolean isPostCard, boolean isBubble) {
        final ProgressDialog progressDialog = UtilsUI.createProgressDialog(activity, R.string.processing_image, true);

        String content = null;
        if (isPostCard && quote != null) {
            content = quote.getContent();
        }

        PostCardRenderer.requestPrepareSharingImage(activity, imageUri, content, isBubble).subscribe(success -> {
            progressDialog.dismiss();
            if (success) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setPackage(packageName);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, PostCardRenderer.getShareFileUri(activity));
                if (!isPostCard && quote != null) {
                    shareIntent.putExtra(Intent.EXTRA_TEXT, quote.getContent());
                }

                if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(shareIntent);
                } else {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                }
            } else {
                UtilsUI.showErrorInSnackBar(activity, activity.getString(R.string.processing_image_error));
            }
        });
    }

    public static void shareGifImage(Activity activity, Uri gifUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, gifUrl);
        activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.app_name)));
    }

    public static void shareGifByPackageName(Activity activity, String packageName, Uri gifUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage(packageName);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, gifUrl);
        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(shareIntent);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        }
    }

    public static void shareGifByEmail(Activity activity, Uri gifUrl) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_STREAM, gifUrl);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void shareText(Context context, String text) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.app_name)));
    }

    public static Observable<Boolean> requestSetImageAsWallpaper(final Context context, final String imagePath) {
        return Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
                PostCardRenderer.updateShareFileUri();
                File imageFile = PostCardRenderer.downloadFile(context, imagePath, PostCardRenderer.getShareFileName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
                    String mime = "image/*";
                    intent.setDataAndType(PostCardRenderer.getShareFileUri(context), mime);
                    context.startActivity(intent);
                } else {
                    wallpaperManager.setBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                }
                subscriber.onNext(true);
            } catch (Exception e) {
                Logger.e(e.toString());
                subscriber.onNext(false);
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private static void shareImageAndText(Context context, String quoteText) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, quoteText);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, PostCardRenderer.getShareFileUri(context));
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.app_name)));
    }

}
