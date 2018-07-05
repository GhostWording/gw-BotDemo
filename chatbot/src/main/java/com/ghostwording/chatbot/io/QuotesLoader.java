package com.ghostwording.chatbot.io;

import android.content.Context;

import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QuotesLoader {

    private static QuotesLoader instance;

    private QuoteDatabase quoteDatabase;

    public static void init(Context context) {
        instance = new QuotesLoader(context);
    }

    public static QuotesLoader instance() {
        return instance;
    }

    private QuotesLoader(Context context) {
        quoteDatabase = new QuoteDatabase(context);
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

}
