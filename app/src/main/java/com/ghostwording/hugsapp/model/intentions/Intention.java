package com.ghostwording.hugsapp.model.intentions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Intention implements Parcelable {

    @Expose
    private String AreaId;
    @Expose
    private Integer SortOrderInArea;
    @Expose
    private Boolean HasImage;
    @Expose
    private String ImagePath;
    @Expose
    private String IntentionId;
    @Expose
    private String UpdateDate;
    @Expose
    private Integer SortOrder;
    @Expose
    private String MediaUrl;
    @Expose
    private String Culture;
    @Expose
    private Object Description;
    @Expose
    private List<Label> Labels = new ArrayList<>();
    @Expose
    private String Slug;
    @Expose
    private String SlugPrototypeLink;
    @Expose
    private String Label;
    @Expose
    private String MostRecentTextUpdate;
    @Expose
    private Integer MostRecentTextUpdateEpoch;
    @Expose
    private Integer WeightingCoefficient;
    @Expose
    private String Recurring;
    @Expose
    private String Impersonal;
    @Expose
    private String RelationTypesString;

    public Intention() {

    }

    protected Intention(Parcel in) {
        AreaId = in.readString();
        ImagePath = in.readString();
        IntentionId = in.readString();
        UpdateDate = in.readString();
        MediaUrl = in.readString();
        Culture = in.readString();
        Slug = in.readString();
        SlugPrototypeLink = in.readString();
        Label = in.readString();
        MostRecentTextUpdate = in.readString();
        Recurring = in.readString();
        Impersonal = in.readString();
        RelationTypesString = in.readString();
    }

    public static final Creator<Intention> CREATOR = new Creator<Intention>() {
        @Override
        public Intention createFromParcel(Parcel in) {
            return new Intention(in);
        }

        @Override
        public Intention[] newArray(int size) {
            return new Intention[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AreaId);
        dest.writeString(ImagePath);
        dest.writeString(IntentionId);
        dest.writeString(UpdateDate);
        dest.writeString(MediaUrl);
        dest.writeString(Culture);
        dest.writeString(Slug);
        dest.writeString(SlugPrototypeLink);
        dest.writeString(Label);
        dest.writeString(MostRecentTextUpdate);
        dest.writeString(Recurring);
        dest.writeString(Impersonal);
        dest.writeString(RelationTypesString);
    }

    /**
     * @return The AreaId
     */
    public String getAreaId() {
        return AreaId;
    }

    /**
     * @param AreaId The AreaId
     */
    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    /**
     * @return The SortOrderInArea
     */
    public Integer getSortOrderInArea() {
        return SortOrderInArea;
    }

    /**
     * @param SortOrderInArea The SortOrderInArea
     */
    public void setSortOrderInArea(Integer SortOrderInArea) {
        this.SortOrderInArea = SortOrderInArea;
    }

    /**
     * @return The HasImage
     */
    public Boolean getHasImage() {
        return HasImage;
    }

    /**
     * @param HasImage The HasImage
     */
    public void setHasImage(Boolean HasImage) {
        this.HasImage = HasImage;
    }

    /**
     * @return The ImagePath
     */
    public String getImagePath() {
        return ImagePath;
    }

    /**
     * @param ImagePath The ImagePath
     */
    public void setImagePath(String ImagePath) {
        this.ImagePath = ImagePath;
    }

    /**
     * @return The IntentionId
     */
    public String getIntentionId() {
        return IntentionId;
    }

    /**
     * @param IntentionId The IntentionId
     */
    public void setIntentionId(String IntentionId) {
        this.IntentionId = IntentionId;
    }

    /**
     * @return The UpdateDate
     */
    public String getUpdateDate() {
        return UpdateDate;
    }

    /**
     * @param UpdateDate The UpdateDate
     */
    public void setUpdateDate(String UpdateDate) {
        this.UpdateDate = UpdateDate;
    }

    /**
     * @return The SortOrder
     */
    public Integer getSortOrder() {
        return SortOrder;
    }

    /**
     * @param SortOrder The SortOrder
     */
    public void setSortOrder(Integer SortOrder) {
        this.SortOrder = SortOrder;
    }

    /**
     * @param MediaUrl The MediaUrl
     */
    public void setMediaUrl(String MediaUrl) {
        this.MediaUrl = MediaUrl;
    }

    /**
     * @return The MediaUrl
     */
    public String getMediaUrl() {
        return MediaUrl;
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
     * @return The Description
     */
    public Object getDescription() {
        return Description;
    }

    /**
     * @param Description The Description
     */
    public void setDescription(Object Description) {
        this.Description = Description;
    }

    /**
     * @return The Labels
     */
    public List<Label> getLabels() {
        return Labels;
    }

    public String getSearchKeyword() {
        for (Label label : getLabels()) {
            if (label.getCulture().contains("en")) {
                return label.getLabel();
            }
        }
        return null;
    }

    /**
     * @param Labels The Labels
     */
    public void setLabels(List<Label> Labels) {
        this.Labels = Labels;
    }

    /**
     * @return The Slug
     */
    public String getSlug() {
        return Slug;
    }

    /**
     * @param Slug The Slug
     */
    public void setSlug(String Slug) {
        this.Slug = Slug;
    }

    /**
     * @return The SlugPrototypeLink
     */
    public String getSlugPrototypeLink() {
        return SlugPrototypeLink;
    }

    /**
     * @param SlugPrototypeLink The SlugPrototypeLink
     */
    public void setSlugPrototypeLink(String SlugPrototypeLink) {
        this.SlugPrototypeLink = SlugPrototypeLink;
    }

    /**
     * @return The Label
     */
    public String getLabel() {
        return Label;
    }

    /**
     * @param Label The Label
     */
    public void setLabel(String Label) {
        this.Label = Label;
    }

    /**
     * @return The MostRecentTextUpdate
     */
    public String getMostRecentTextUpdate() {
        return MostRecentTextUpdate;
    }

    /**
     * @param MostRecentTextUpdate The MostRecentTextUpdate
     */
    public void setMostRecentTextUpdate(String MostRecentTextUpdate) {
        this.MostRecentTextUpdate = MostRecentTextUpdate;
    }

    /**
     * @return The MostRecentTextUpdateEpoch
     */
    public Integer getMostRecentTextUpdateEpoch() {
        return MostRecentTextUpdateEpoch;
    }

    /**
     * @param MostRecentTextUpdateEpoch The MostRecentTextUpdateEpoch
     */
    public void setMostRecentTextUpdateEpoch(Integer MostRecentTextUpdateEpoch) {
        this.MostRecentTextUpdateEpoch = MostRecentTextUpdateEpoch;
    }

    /**
     * @return The WeightingCoefficient
     */
    public Integer getWeightingCoefficient() {
        return WeightingCoefficient;
    }

    /**
     * @param WeightingCoefficient The WeightingCoefficient
     */
    public void setWeightingCoefficient(Integer WeightingCoefficient) {
        this.WeightingCoefficient = WeightingCoefficient;
    }

    public String getRelationTypesString() {
        return RelationTypesString;
    }

    public void setRelationTypesString(String RelationTypesString) {
        this.RelationTypesString = RelationTypesString;
    }


    /**
     * @return The Recurring
     */
    public String getRecurring() {
        return Recurring;
    }

    /**
     * @param Recurring The Recurring
     */
    public void setRecurring(String Recurring) {
        this.Recurring = Recurring;
    }

    /**
     * @return The Impersonal
     */
    public String getImpersonal() {
        return Impersonal;
    }

    /**
     * @param Impersonal The Impersonal
     */
    public void setImpersonal(String Impersonal) {
        this.Impersonal = Impersonal;
    }

    public class Label {
        @Expose
        private String IntentionId;
        @Expose
        private String Culture;
        @Expose
        private String Label;
        @Expose
        private String Slug;

        /**
         * @return The IntentionId
         */
        public String getIntentionId() {
            return IntentionId;
        }

        /**
         * @param IntentionId The IntentionId
         */
        public void setIntentionId(String IntentionId) {
            this.IntentionId = IntentionId;
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
         * @return The Label
         */
        public String getLabel() {
            return Label;
        }

        /**
         * @param Label The Label
         */
        public void setLabel(String Label) {
            this.Label = Label;
        }

        /**
         * @return The Slug
         */
        public String getSlug() {
            return Slug;
        }

        /**
         * @param Slug The Slug
         */
        public void setSlug(String Slug) {
            this.Slug = Slug;
        }
    }

}
