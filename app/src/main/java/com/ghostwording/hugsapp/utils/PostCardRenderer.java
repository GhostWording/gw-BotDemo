package com.ghostwording.hugsapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ghostwording.hugsapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostCardRenderer {

    private static final int IMAGE_SIZE = 850;
    private static String shareFileName;

    public static Uri getShareGifUri(Context context) {
        return FileProvider.getUriForFile(context,
                "com.wavemining.demobot.fileprovider",
                new File(context.getExternalFilesDir(null) + "/" + Utils.GIF_FILENAME));
    }

    public static String getShareFileName() {
        return shareFileName;
    }

    public static Uri getShareFileUri(Context context) {
        return FileProvider.getUriForFile(context,
                "com.wavemining.demobot.fileprovider",
                new File(context.getExternalFilesDir(null) + "/" + shareFileName));
    }

    public static Observable<Bitmap> renderPostCardPreviewImage(final Context context, final String imagePath, final String quoteText, final boolean isBubble) {
        return Observable.create((ObservableOnSubscribe<Bitmap>) subscriber -> {
            try {
                subscriber.onNext(createPostCardBitmap(context, imagePath, quoteText, isBubble));
            } catch (Exception e) {
                Logger.e(e.toString());
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Boolean> requestDownloadFile(final Context context, final String fileUrl, final String fileName) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                File file = downloadFile(context, fileUrl, fileName);
                subscriber.onNext(file != null);
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static File downloadFile(Context context, String fileUrl, String fileName) {
        File result = new File(context.getExternalFilesDir(null), fileName);
        try {
            File file = Glide.with(context).load(fileUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get(10, TimeUnit.SECONDS);
            copy(file, result);
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return result;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static Observable<Boolean> requestPrepareSharingImage(final Context context, final String imagePath, final String quoteText, final boolean isBubble) {
        return Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
            try {
                Bitmap resultBitmap = createPostCardBitmap(context, imagePath, quoteText, isBubble);
                updateShareFileUri();
                FileOutputStream out = new FileOutputStream(new File(context.getExternalFilesDir(null), shareFileName));
                resultBitmap.compress(isBubble ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
                subscriber.onNext(true);
            } catch (Exception e) {
                Logger.e(e.toString());
                subscriber.onNext(false);
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private static Bitmap createPostCardBitmap(Context context, String imagePath, String quoteText, boolean isBubble) throws ExecutionException, InterruptedException {
        Bitmap imageBitmap = Glide.with(context).
                load(imagePath).
                asBitmap().
                into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).
                get();

        Bitmap resultBitmap;
        if (!TextUtils.isEmpty(quoteText)) {
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, IMAGE_SIZE, IMAGE_SIZE);
            Bitmap textBitmap = getTextBitmap(context, quoteText, isBubble);

            resultBitmap = Bitmap.createBitmap(IMAGE_SIZE, IMAGE_SIZE + textBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(resultBitmap);
            if (isBubble) {
                canvas.drawBitmap(textBitmap, 0, 0, new Paint());
                canvas.drawBitmap(imageBitmap, 0, textBitmap.getHeight(), new Paint());
            } else {
                canvas.drawBitmap(imageBitmap, 0, 0, new Paint());
                canvas.drawBitmap(textBitmap, 0, IMAGE_SIZE, new Paint());
            }
        } else {
            resultBitmap = imageBitmap;
        }
        return resultBitmap;
    }

    private static Bitmap createPostCardBitmap(Context context, String imagePath, String quoteText) throws ExecutionException, InterruptedException {
        Bitmap imageBitmap = Glide.with(context).
                load(imagePath).
                asBitmap().
                into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).
                get();

        Bitmap resultBitmap;
        if (!TextUtils.isEmpty(quoteText)) {
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, IMAGE_SIZE, IMAGE_SIZE);
            Bitmap textBitmap = getTextBitmap(context, quoteText);

            resultBitmap = Bitmap.createBitmap(IMAGE_SIZE, IMAGE_SIZE + textBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(imageBitmap, 0, 0, new Paint());
            canvas.drawBitmap(textBitmap, 0, IMAGE_SIZE, new Paint());
        } else {
            resultBitmap = imageBitmap;
        }
        return resultBitmap;
    }

    private static Bitmap getTextBitmap(Context context, String quoteText) {
        View textLayoutTemplate;
        textLayoutTemplate = LayoutInflater.from(context).inflate(R.layout.template_postcard, null);
        ((TextView) textLayoutTemplate.findViewById(R.id.tv_quote)).setText(quoteText);
        return getBitmapFromView(textLayoutTemplate);
    }

    private static Bitmap getTextBitmap(Context context, String quoteText, boolean isBubble) {
        View textLayoutTemplate;
        if (isBubble) {
            textLayoutTemplate = LayoutInflater.from(context).inflate(R.layout.template_postcard_bubble, null);
        } else {
            textLayoutTemplate = LayoutInflater.from(context).inflate(R.layout.template_postcard, null);
        }
        ((TextView) textLayoutTemplate.findViewById(R.id.tv_quote)).setText(quoteText);
        return getBitmapFromView(textLayoutTemplate);
    }

    public static void updateShareFileUri() {
        shareFileName = System.currentTimeMillis() + "_image.png";
    }

    private static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

}
