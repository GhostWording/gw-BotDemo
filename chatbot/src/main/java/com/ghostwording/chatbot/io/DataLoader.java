package com.ghostwording.chatbot.io;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.model.requests.FragmentRequest;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataLoader {

    private static DataLoader instance;

    private QuoteDatabase quoteDatabase;
    private SequenceDatabase sequenceDatabase;

    public static void init(Context context) {
        instance = new DataLoader(context);
    }

    public static DataLoader instance() {
        return instance;
    }

    private DataLoader(Context context) {
        quoteDatabase = new QuoteDatabase(context);
        sequenceDatabase = new SequenceDatabase(context);
    }

    public void saveSequences(List<BotSequence> sequences) {
        for (BotSequence sequence : sequences) {
            sequenceDatabase.saveSequence(sequence);
        }
    }

    public List<BotSequence> getAllBotSequences() {
        return sequenceDatabase.getAllSequences();
    }

    public Observable<BotSequence> loadSequenceById(final String sequenceId) {
        return Observable.create((ObservableOnSubscribe<BotSequence>) subscriber -> {
            BotSequence result;
            result = sequenceDatabase.getSequenceById(sequenceId);
            if (result == null) {
                result = ApiClient.getInstance().botService.getFragment(new FragmentRequest(AppConfiguration.getBotName(), null, sequenceId)).execute().body();
                sequenceDatabase.saveSequence(result);
            }
            subscriber.onNext(result);
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Quote> getQuoteById(final String textId) {
        return Observable.create((ObservableOnSubscribe<Quote>) subscriber -> {
            Quote result;
            try {
                result = quoteDatabase.getQuoteById(textId);
                if (result == null) {
                    result = ApiClient.getInstance().coreApiService.getTextById(AppConfiguration.getAppAreaId(), textId).execute().body();
                    quoteDatabase.saveQuote(result);
                }
                subscriber.onNext(result);
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public void saveQuotes(List<Quote> quotes) {
        for (Quote quote : quotes) {
            quoteDatabase.saveQuote(quote);
        }
    }

    public static String getImageUrl(Context context, String imageUrl) {
        File cachedFile = new File(context.getExternalFilesDir(null), getMD5(imageUrl));
        if (cachedFile.exists()) {
            return Uri.fromFile(cachedFile).toString();
        } else {
            return imageUrl;
        }
    }

    public static File downloadFile(Context context, String fileUrl) {
        File result = new File(context.getExternalFilesDir(null), getMD5(fileUrl));
        if (result.exists()) {
            return result;
        }
        try {
            File file = Glide.with(context).load(fileUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get(10, TimeUnit.SECONDS);
            copy(file, result);
        } catch (Exception e) {
            Logger.e("Download file error : " + e.toString());
        }
        return result;
    }

    private static String getMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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

}

