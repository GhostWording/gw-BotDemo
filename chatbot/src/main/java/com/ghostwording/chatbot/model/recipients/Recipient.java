package com.ghostwording.chatbot.model.recipients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Recipient {

    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("ImageUrl")
    @Expose
    private String ImageUrl;
    @SerializedName("RelationTypeTag")
    @Expose
    private String RelationTypeTag;
    @SerializedName("Gender")
    @Expose
    private String Gender;
    @SerializedName("UsualRecipient")
    @Expose
    private Boolean UsualRecipient;
    @SerializedName("SubscribableRecipient")
    @Expose
    private Boolean SubscribableRecipient;
    @SerializedName("PoliteForm")
    @Expose
    private String PoliteForm;
    @SerializedName("Importance")
    @Expose
    private Integer Importance;
    @SerializedName("Labels")
    @Expose
    private List<Label> Labels = new ArrayList<>();

    public String getLocalizedLabel() {
        for (Recipient.Label label : Labels) {
            if (label.getCulture().contains(Locale.getDefault().getLanguage())) {
                return label.getUserLabel();
            }
        }
        return "";
    }

    /**
     * @return The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id The Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * @return The ImageUrl
     */
    public String getImageUrl() {
        return ImageUrl;
    }

    /**
     * @param ImageUrl The ImageUrl
     */
    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    /**
     * @return The RelationTypeTag
     */
    public String getRelationTypeTag() {
        return RelationTypeTag;
    }

    /**
     * @param RelationTypeTag The RelationTypeTag
     */
    public void setRelationTypeTag(String RelationTypeTag) {
        this.RelationTypeTag = RelationTypeTag;
    }

    /**
     * @return The Gender
     */
    public String getGender() {
        return Gender;
    }

    /**
     * @param Gender The Gender
     */
    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    /**
     * @return The UsualRecipient
     */
    public Boolean getUsualRecipient() {
        return UsualRecipient;
    }

    /**
     * @param UsualRecipient The UsualRecipient
     */
    public void setUsualRecipient(Boolean UsualRecipient) {
        this.UsualRecipient = UsualRecipient;
    }

    /**
     * @return The SubscribableRecipient
     */
    public Boolean getSubscribableRecipient() {
        return SubscribableRecipient;
    }

    /**
     * @param SubscribableRecipient The SubscribableRecipient
     */
    public void setSubscribableRecipient(Boolean SubscribableRecipient) {
        this.SubscribableRecipient = SubscribableRecipient;
    }

    /**
     * @return The PoliteForm
     */
    public String getPoliteForm() {
        return PoliteForm;
    }

    /**
     * @param PoliteForm The PoliteForm
     */
    public void setPoliteForm(String PoliteForm) {
        this.PoliteForm = PoliteForm;
    }

    /**
     * @return The Importance
     */
    public Integer getImportance() {
        return Importance;
    }

    /**
     * @param Importance The Importance
     */
    public void setImportance(Integer Importance) {
        this.Importance = Importance;
    }

    /**
     * @return The Labels
     */
    public List<Label> getLabels() {
        return Labels;
    }

    /**
     * @param Labels The Labels
     */
    public void setLabels(List<Label> Labels) {
        this.Labels = Labels;
    }

    public static class Label {

        @SerializedName("RecipientTypeId")
        @Expose
        private String RecipientTypeId;
        @SerializedName("Culture")
        @Expose
        private String Culture;
        @SerializedName("UserLabel")
        @Expose
        private String UserLabel;
        @SerializedName("AppLabel")
        @Expose
        private String AppLabel;

        /**
         * @return The RecipientTypeId
         */
        public String getRecipientTypeId() {
            return RecipientTypeId;
        }

        /**
         * @param RecipientTypeId The RecipientTypeId
         */
        public void setRecipientTypeId(String RecipientTypeId) {
            this.RecipientTypeId = RecipientTypeId;
        }

        /**
         * @return The Culture
         */
        public String getCulture() {
            return Culture;
        }

        /**
         * @param Culture The Culture
         */
        public void setCulture(String Culture) {
            this.Culture = Culture;
        }

        /**
         * @return The UserLabel
         */
        public String getUserLabel() {
            return UserLabel;
        }

        /**
         * @param UserLabel The UserLabel
         */
        public void setUserLabel(String UserLabel) {
            this.UserLabel = UserLabel;
        }

        /**
         * @return The AppLabel
         */
        public String getAppLabel() {
            return AppLabel;
        }

        /**
         * @param AppLabel The AppLabel
         */
        public void setAppLabel(String AppLabel) {
            this.AppLabel = AppLabel;
        }

    }

}