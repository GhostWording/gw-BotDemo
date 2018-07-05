package com.ghostwording.chatbot.analytics;

import android.database.Cursor;

import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class EventModel {

    @SerializedName("AreaId")
    @Expose
    public final String areaId = AppConfiguration.getAppAreaAnalytics();

    @SerializedName("DeviceId")
    @Expose
    public final String deviceId = AppConfiguration.getDeviceId();

    @SerializedName("Last")
    @Expose
    private Boolean last = AnalyticsHelper.isLast();

    @SerializedName("LanguageCode")
    @Expose
    public final String languageCode = Locale.getDefault().getLanguage();

    @SerializedName("FacebookId")
    @Expose
    public final String facebookId = PrefManager.instance().getFacebookId();

    @SerializedName("VersionNumber")
    @Expose
    public final String versionNumber = AppConfiguration.getAppVersionNumber();

    @SerializedName("OsType")
    @Expose
    public final String osType = "android";

    @SerializedName("ExperimentId")
    @Expose
    public final String experimentId = PrefManager.instance().getExperimentId();

    @SerializedName("VariationId")
    @Expose
    public final Integer variationId = PrefManager.instance().getVariationId();

    @SerializedName("IsNewInstall")
    @Expose
    public final Boolean isNewInstall = PrefManager.instance().isNewInstall();

    @SerializedName("ActionType")
    @Expose
    private String actionType;

    @SerializedName("ActionLocation")
    @Expose
    private String actionLocation;

    @SerializedName("TargetType")
    @Expose
    private String targetType;

    @SerializedName("TargetId")
    @Expose
    private String targetId;

    @SerializedName("TargetParameter")
    @Expose
    private String targetParameter;

    @SerializedName("EventTime")
    @Expose
    private long clientTime;

    @SerializedName("Context")
    @Expose
    private String context;

    @SerializedName("RecipientType")
    @Expose
    private String recipientType;

    @SerializedName("IntentionId")
    @Expose
    private String intentionId;

    @SerializedName("RecipientId")
    @Expose
    private String recipientId;

    private int id;

    public EventModel() {

    }

    public EventModel(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.ID));
        actionType = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.ACTION_TYPE));
        actionLocation = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.ACTION_LOCATION));
        targetType = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.TARGET_TYPE));
        targetId = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.TARGET_ID));
        targetParameter = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.TARGET_PARAMETER));
        clientTime = cursor.getLong(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.CLIENT_TIME));
        context = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.CONTEXT));
        recipientType = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.RECIPIENT_TYPE));
        recipientId = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.RECIPIENT_ID));
        intentionId = cursor.getString(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.INTENTION_ID));
        last = cursor.getInt(cursor.getColumnIndex(EventsDatabase.TABLE_EVENTS.LAST)) == 1;
    }

    public Integer getId() {
        return id;
    }

    public String getActionType() {
        return actionType;
    }

    public EventModel setActionType(String actionType) {
        this.actionType = actionType;
        return this;
    }

    public EventModel setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getActionLocation() {
        return actionLocation;
    }

    public EventModel setActionLocation(String actionLocation) {
        this.actionLocation = actionLocation;
        return this;
    }

    public String getTargetType() {
        return targetType;
    }

    public EventModel setTargetType(String targetType) {
        this.targetType = targetType;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public EventModel setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getTargetParameter() {
        return targetParameter;
    }

    public EventModel setTargetParameter(String targetParameter) {
        this.targetParameter = targetParameter;
        return this;
    }

    public long getClientTime() {
        return clientTime;
    }

    public EventModel setClientTime(long clientTime) {
        this.clientTime = clientTime;
        return this;
    }

    public EventModel setIntentionId(String intentionId) {
        this.intentionId = intentionId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getIntentionId() {
        return intentionId;
    }

    public EventModel setContext(String context) {
        this.context = context;
        return this;
    }

    public EventModel setRecipientType(String recipientType) {
        this.recipientType = recipientType;
        return this;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public String getContext() {
        return context;
    }

    public Boolean isLast() {
        return last;
    }

    public void setLast(boolean isLast) {
        last = isLast;
    }


}
