package com.ghostwording.chatbot.textimagepreviews;

import android.content.Context;
import android.content.Intent;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.model.MoodMenuItem;
import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.intentions.Intention;
import com.ghostwording.chatbot.model.recipients.Recipient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PickHelper {

    public interface ImageThemes {
        String ANIMAL = "themes/profileAnimals";
        String FLOWERS = "themes/profileFlowers";
        String LANDSCAPE = "themes/profileLandscapes";
        String DOGS = "themes/puppies";
        String CATS = "themes/kittens";
        String NATURE = "themes/nature";
        String SIMPLE_FLOWERS = "themes/flowers";
        String RELATIONSHIP = "themes/pairs";
        String EMOJI = "themes/emoticons";
    }

    private static PickHelper instance;

    public static synchronized PickHelper with(Context context) {
        if (instance == null) {
            instance = new PickHelper(context.getApplicationContext());
        }
        return instance;
    }

    private Context context;
    private PublishSubject<String> imagePublishSubject;
    private PublishSubject<TextResult> textPublishSubject;
    private PublishSubject<Recipient> recipientPublishSubject;
    private PublishSubject<Intention> intentionPublishSubject;
    private PublishSubject<MoodMenuItem> categoryPublishSubject;

    private List<PopularImages.Image> predefinedImages;
    private String selectedIntentionId;

    private PickHelper(Context context) {
        this.context = context;
    }

    public Observable<Recipient> pickRecipient() {
        recipientPublishSubject = PublishSubject.create();
        Intent intent = new Intent(context, PickRecipientActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return recipientPublishSubject;
    }

    public Observable<Intention> pickIntention(String relationTag) {
        return pickIntention(relationTag, null, null);
    }

    public Observable<Intention> pickIntention(String relationTag, String selectedIntentionId) {
        return pickIntention(relationTag, selectedIntentionId, null);
    }

    public Observable<Intention> pickIntention(String relationTag, String selectedIntentionId, String areaId) {
        this.selectedIntentionId = selectedIntentionId;
        intentionPublishSubject = PublishSubject.create();
        Intent intent = new Intent(context, PickIntentionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PickIntentionActivity.RELATION_TYPE_TAG, relationTag);
        intent.putExtra(PickIntentionActivity.AREA_ID, areaId);
        context.startActivity(intent);
        return intentionPublishSubject;
    }

    public void onImagePicked(String result) {
        if (imagePublishSubject != null) {
            imagePublishSubject.onNext(result);
            imagePublishSubject.onComplete();
        }
    }

    void onTextPicked(TextResult textResult) {
        if (textPublishSubject != null) {
            textPublishSubject.onNext(textResult);
            textPublishSubject.onComplete();
        }
    }

    void onRecipientPicked(Recipient recipient) {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.PICK_RECIPIENT, recipient.getId());
        if (recipientPublishSubject != null) {
            recipientPublishSubject.onNext(recipient);
            recipientPublishSubject.onComplete();
        }
    }

    void onIntentionPicked(Intention intention) {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.INTENTION, AnalyticsHelper.Events.PICK_INTENTION, intention.getIntentionId());
        if (intentionPublishSubject != null) {
            intentionPublishSubject.onNext(intention);
            intentionPublishSubject.onComplete();
        }
    }

    void onCategoryPicked(MoodMenuItem category) {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.INTENTION, AnalyticsHelper.Events.PICK_CATEGORY, category.getImagePath());
        if (categoryPublishSubject != null) {
            categoryPublishSubject.onNext(category);
            categoryPublishSubject.onComplete();
        }
    }

    public List<PopularImages.Image> getPredefinedImages() {
        return predefinedImages;
    }

    public String getSelectedIntentionId() {
        return selectedIntentionId;
    }

    public static class TextResult {
        public final String textId;
        public final String text;

        public TextResult(String textId, String text) {
            this.textId = textId;
            this.text = text;
        }
    }

}
