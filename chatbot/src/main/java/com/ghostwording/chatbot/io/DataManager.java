package com.ghostwording.chatbot.io;

import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.ChatMessage;
import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.model.GifIdsResponse;
import com.ghostwording.chatbot.model.GifImages;
import com.ghostwording.chatbot.model.GifResponse;
import com.ghostwording.chatbot.model.MediaModel;
import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.VotingCounters;
import com.ghostwording.chatbot.model.WeightAble;
import com.ghostwording.chatbot.model.texts.PopularTexts;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.model.texts.QuoteLanguageComparator;
import com.ghostwording.chatbot.model.texts.QuoteShareComparator;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.UtilsMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataManager {

    private static final String SURVEY = "survey-";
    private static final String SURVEY_COUNTRY = "surveyPerCountry-";
    private static final String SURVEY_PER_GENDER = "surveyPerGender-";
    private static final String SURVEY_PER_MATURITY = "surveyPerMaturity-";

    private static int countVotes;

    public static Observable<List<Quote>> loadPopularTexts(final String imageName) {
        return Observable.create((ObservableOnSubscribe<List<Quote>>) subscriber -> {
            try {
                final int MIN_QUOTES_COUNT = 10;
                List<Quote> result = new ArrayList<>();

                //first we display popular texts relevant to intention and has 2+ shares with this image)
                List<PopularTexts> popularTextResponse = ApiClient.getInstance().popularService.getPopularTextsForImage(AppConfiguration.getAppAreaId(), imageName).execute().body();
                if (popularTextResponse != null && popularTextResponse.size() > 0) {
                    List<Quote> popularTexts = popularTextResponse.get(0).getTexts();
                    QuotesLoader.instance().saveQuotes(popularTexts);
                    popularTexts = UtilsMessages.filterQuotes(popularTexts, PrefManager.instance().getSelectedGender());
                    popularTexts = UtilsMessages.filterPopularTexts(popularTexts);
                    result.addAll(popularTexts);
                }

                //10 most popular texts from Facebook status
                if (result.size() < MIN_QUOTES_COUNT) {
                    List<Quote> quotes = ApiClient.getInstance().coreApiService.getQuotes(AppConfiguration.getAppAreaId(), "2E2986").execute().body();
                    QuotesLoader.instance().saveQuotes(quotes);
                    quotes = UtilsMessages.filterQuotes(quotes, PrefManager.instance().getSelectedGender());
                    List<WeightAble> weightAbles = new ArrayList<>();
                    weightAbles.addAll(quotes);

                    for (int i = 0; i < Math.min(MIN_QUOTES_COUNT, quotes.size()); i++) {
                        int randomIndex = UtilsMessages.getRandomIndexBasedOnWeight(weightAbles);
                        result.add(quotes.get(randomIndex));
                        weightAbles.remove(randomIndex);
                        quotes.remove(randomIndex);
                    }
                }

                if (!PrefManager.instance().isFirstTimeAction(imageName)) {
                    Collections.shuffle(result);
                }
                subscriber.onNext(result);
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ChatMessage.BotMessage> loadImageCardFromPrototype(BotSequence.Step step) {
        return Observable.create((ObservableOnSubscribe<ChatMessage.BotMessage>) subscriber -> {
            String prototypeId = step.getParameters().getPrototypeId();
            ChatMessage.BotMessage result = new ChatMessage.BotMessage();
            List<Quote> quotes = ApiClient.getInstance().coreApiService.getQuotesFromRealizations(AppConfiguration.getAppAreaId(), prototypeId).execute().body();
            quotes = UtilsMessages.filterQuotes(quotes, PrefManager.instance().getSelectedGender(), false);
            Collections.sort(quotes, new QuoteLanguageComparator());
            Quote selectedMessage = quotes.get(0);
            result.setPrototypeId(selectedMessage.getPrototypeId());
            result.setTextId(selectedMessage.getTextId());
            result.setContent(selectedMessage.getContent());

            if (step.getParameters().getDefaultImage() != null) {
                result.setImageLink(step.getParameters().getDefaultImage().getImagePath());
            } else {
                List<PopularImages> popularImages = ApiClient.getInstance().popularService.getPopularImagesForText(selectedMessage.getPrototypeId()).execute().body();
                if (popularImages == null || popularImages.size() == 0) {
                    popularImages = ApiClient.getInstance().popularService.getPopularImages(selectedMessage.getIntentionId()).execute().body();
                }
                List<PopularImages.Image> images = popularImages.get(0).getImages();
                result.setImageLink(images.get(new Random().nextInt(images.size())).getImageLink());
            }

            subscriber.onNext(result);
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Quote>> loadPopularTextsForIntention(final String intentionId) {
        return Observable.create((ObservableOnSubscribe<List<Quote>>) subscriber -> {
            try {
                List<Quote> result = new ArrayList<>();

                //10 most popular texts from intention
                List<Quote> quotes = ApiClient.getInstance().coreApiService.getQuotes(AppConfiguration.getAppAreaId(), intentionId).execute().body();
                QuotesLoader.instance().saveQuotes(quotes);
                quotes = UtilsMessages.filterQuotes(quotes, PrefManager.instance().getSelectedGender());
                List<WeightAble> weightAbles = new ArrayList<>();
                weightAbles.addAll(quotes);

                for (int i = 0; i < Math.min(10, quotes.size()); i++) {
                    int randomIndex = UtilsMessages.getRandomIndexBasedOnWeight(weightAbles);
                    result.add(quotes.get(randomIndex));
                    weightAbles.remove(randomIndex);
                    quotes.remove(randomIndex);
                }

                subscriber.onNext(result);
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Quote>> requestPopularTextsForIntention(final String intentionId) {
        return Observable.create(new ObservableOnSubscribe<List<Quote>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Quote>> subscriber) throws Exception {
                try {
                    List<Quote> result = null;

                    List<PopularTexts> popularTexts = ApiClient.getInstance().popularService.getPopularTexts(AppConfiguration.getAppAreaId(), intentionId).execute().body();
                    if (popularTexts != null && popularTexts.size() > 0) {
                        result = popularTexts.get(0).getTexts();
                        Collections.sort(result, new QuoteShareComparator());
                        result = UtilsMessages.filterQuotes(result, PrefManager.instance().getSelectedGender());
                    }

                    if (result == null || result.size() < 5) {
                        List<Quote> quotes = ApiClient.getInstance().coreApiService.getQuotes(AppConfiguration.getAppAreaId(), intentionId).execute().body();
                        quotes = UtilsMessages.filterQuotes(quotes, PrefManager.instance().getSelectedGender());
                        Collections.shuffle(quotes);
                        result = new ArrayList<>();
                        for (int i = 0; i < Math.min(quotes.size(), 30); i++) {
                            result.add(quotes.get(i));
                        }
                    }
                    subscriber.onNext(result);
                } catch (Exception ex) {
                    Logger.e(ex.toString());
                }
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<String> loadHugGifImage() {
        return Observable.create((ObservableOnSubscribe<String>) subscriber -> {
            GifImages gifImages = ApiClient.getInstance().configService.getHugGifs().execute().body();
            StringBuilder stringBuilder = new StringBuilder();
            String delimiter = "";
            for (GifImages.Image id : gifImages.getImages()) {
                stringBuilder.append(delimiter + id.getPath());
                delimiter = ",";
            }
            GifResponse gifResponse = ApiClient.getInstance().giffyService.getGifByIds(stringBuilder.toString()).execute().body();
            GifResponse.GifImage gifImage = gifResponse.getData().get(new Random().nextInt(gifResponse.getData().size()));
            subscriber.onNext(gifImage.getImages().getFixedHeight().getUrl());
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<MediaModel>> loadMediaMixedContent() {
        return Observable.create((ObservableOnSubscribe<List<MediaModel>>) subscriber -> {
            final int COUNT_MEDIA_CONTENT = 15;
            List<MediaModel> result = new ArrayList<>();
            String imagePath = null;
            int randomImagePath = new Random().nextInt(5);
            switch (randomImagePath) {
                case 0:
                    imagePath = "data/common/gifsnewformat/hugslove.json";
                    break;
                case 1:
                    imagePath = "data/common/gifsnewformat/hugsdog.json";
                    break;
                case 2:
                    imagePath = "data/common/gifsnewformat/hugscartoon.json";
                    break;
                case 3:
                    imagePath = "data/common/gifsnewformat/hugsanimals.json";
                    break;
            }

            if (imagePath != null) {
                GifImages gifIdsResponse = ApiClient.getInstance().configService.getGiffsByPath(imagePath).execute().body();
                StringBuilder stringBuilder = new StringBuilder();
                String delimiter = "";
                for (GifImages.Image id : gifIdsResponse.getImages()) {
                    stringBuilder.append(delimiter + id.getPath());
                    delimiter = ",";
                }
                List<GifResponse.GifImage> gifImages = ApiClient.getInstance().giffyService.getGifByIds(stringBuilder.toString()).execute().body().getData();
                Collections.shuffle(gifImages);
                for (int i = 0; i < Math.min(COUNT_MEDIA_CONTENT, gifImages.size()); i++) {
                    result.add(new MediaModel(gifImages.get(i)));
                }
            } else {
                List<String> pictures = ApiClient.getInstance().pictureService.getPicturesByPath("themes/hugs2").execute().body();
                Collections.shuffle(pictures);
                for (int i = 0; i < Math.min(COUNT_MEDIA_CONTENT, pictures.size()); i++) {
                    result.add(new MediaModel(PictureService.HOST_URL + pictures.get(i)));
                }
            }
            subscriber.onNext(result);
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<GifResponse> requestTrendingGifs(final String gifPath) {
        return Observable.create((ObservableOnSubscribe<GifResponse>) emitter -> {
            try {
                GifImages gifIdsResponse = ApiClient.getInstance().configService.getGiffsByPath(gifPath).execute().body();
                StringBuilder stringBuilder = new StringBuilder();
                String delimiter = "";
                for (GifImages.Image id : gifIdsResponse.getImages()) {
                    stringBuilder.append(delimiter + id.getPath());
                    delimiter = ",";
                }
                GifResponse result = ApiClient.getInstance().giffyService.getGifByIds(stringBuilder.toString()).execute().body();
                if (result != null) {
                    emitter.onNext(result);
                }
            } catch (Exception ex) {
                Logger.e("Gifs loading error", ex);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<GifResponse> requestTrendingGifs() {
        return Observable.create((ObservableOnSubscribe<GifResponse>) emitter -> {
            try {
                GifIdsResponse gifIdsResponse = ApiClient.getInstance().configService.getTrendingGifIds().execute().body();
                StringBuilder stringBuilder = new StringBuilder();
                String delimiter = "";
                for (String id : gifIdsResponse.getAll()) {
                    stringBuilder.append(delimiter + id);
                    delimiter = ",";
                }
                GifResponse result = ApiClient.getInstance().giffyService.getGifByIds(stringBuilder.toString()).execute().body();
                if (result != null) {
                    emitter.onNext(result);
                }
            } catch (Exception ex) {
                Logger.e("Gifs loading error", ex);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Boolean> vote(final BotSequence question, final BotSequence answer) {
        return Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
            try {

                String selectedCounter = answer.getId();
                List<String> votingCounters = new ArrayList<>();
                votingCounters.add(SURVEY + question.getId() + "-" + selectedCounter);
                votingCounters.add(SURVEY_COUNTRY + question.getId() + "-" + selectedCounter + "-" + PrefManager.instance().getUserCountry());
                votingCounters.add(SURVEY_PER_GENDER + question.getId() + "-" + selectedCounter + "-" + PrefManager.instance().getGenderString());
                votingCounters.add(SURVEY_PER_MATURITY + question.getId() + "-" + selectedCounter + "-" + PrefManager.instance().getUserMaturity());
                ApiClient.getInstance().votingService.vote(new VotingCounters(votingCounters)).execute();
                subscriber.onNext(true);
            } catch (Exception ex) {
                subscriber.onNext(false);
                Logger.e(ex.toString());
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ChatMessage.VoteMessage> getSurveyResult(final BotSequence question, final BotSequence answer) {
        return Observable.create((ObservableOnSubscribe<ChatMessage.VoteMessage>) subscriber -> {
            String selectedCounter = answer.getId();
            Integer percentOverall = getCounterResult(SURVEY, question, selectedCounter, "");
            int overallCountVotes = countVotes;
            Integer percentCountry = getCounterResult(SURVEY_COUNTRY, question, selectedCounter, "-" + PrefManager.instance().getUserCountry());
            Integer percentPerGender = getCounterResult(SURVEY_PER_GENDER, question, selectedCounter, "-" + PrefManager.instance().getGenderString());
            Integer percentMaturity = getCounterResult(SURVEY_PER_MATURITY, question, selectedCounter, "-" + PrefManager.instance().getUserMaturity());

            ChatMessage.VoteMessage result = new ChatMessage.VoteMessage(question, answer, percentOverall, overallCountVotes);
            result.setPercentCountry(percentCountry);
            result.setPercentGender(percentPerGender);
            result.setPercentMaturity(percentMaturity);
            subscriber.onNext(result);
            subscriber.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    private static Integer getCounterResult(String counterName, BotSequence question, String selectedCounter, String additionalParameter) throws IOException {
        String firstCounterName = counterName + question.getId() + "-" + question.getCommands().get(0).getId() + additionalParameter;
        String secondCounterName = counterName + question.getId() + "-" + question.getCommands().get(1).getId() + additionalParameter;
        VotingCounters.VotingCounter vote1 = ApiClient.getInstance().votingService.getVotes(firstCounterName).execute().body();
        VotingCounters.VotingCounter vote2 = ApiClient.getInstance().votingService.getVotes(secondCounterName).execute().body();
        countVotes = vote1.getValue() + vote2.getValue();
        Integer result = null;
        if (countVotes > 0) {
            if (question.getCommands().get(0).getId().equals(selectedCounter)) {
                result = (int) (vote1.getValue() * 100.0f / countVotes);
            } else {
                result = (int) (vote2.getValue() * 100.0f / countVotes);
            }
        }
        return result;
    }

}

