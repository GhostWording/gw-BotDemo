package com.ghostwording.hugsapp.model.texts;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ghostwording.hugsapp.io.QuoteDatabase;
import com.ghostwording.hugsapp.model.WeightAble;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Quote implements WeightAble, Parcelable {

    @Expose
    private String TextId;
    @Expose
    private String IntentionId;
    @Expose
    private String IntentionPrototypeSlug;
    @Expose
    private String PrototypeId;
    @Expose
    private String Content;
    @Expose
    private Integer SortBy;
    @Expose
    private String Sender;
    @Expose
    private String Target;
    @Expose
    private String PoliteForm;
    @Expose
    private String Culture;
    @Expose
    private List<String> TagIds = new ArrayList<>();
    @Expose
    private List<String> AvailableCultures = new ArrayList<>();
    @Expose
    private String Status;
    @Expose
    private List<String> OtherRealizationIds = new ArrayList<>();
    @Expose
    private String TagsString;
    @Expose
    private String RealizationIdsString;
    @Expose
    private String CulturesString;
    @Expose
    private Integer RankForIntention;
    @Expose
    private Integer NbSharesForIntention;
    @Expose
    private Integer NbDisplaysForIntention;
    @SerializedName("Scoring")
    @Expose
    private Scoring Scoring;

    public Quote() {

    }

    public Quote(Cursor cursor) {
        TextId = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.TEXT_ID));
        IntentionId = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.INTENTION_ID));
        PrototypeId = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.PROTOTYPE_ID));
        Content = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.CONTENT));
        Sender = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.SENDER));
        Target = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.TARGET));
        PoliteForm = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.POLITE_FORM));
        Culture = cursor.getString(cursor.getColumnIndex(QuoteDatabase.TABLE_QUOTES.CULTURE));
    }


    protected Quote(Parcel in) {
        TextId = in.readString();
        IntentionId = in.readString();
        IntentionPrototypeSlug = in.readString();
        PrototypeId = in.readString();
        Content = in.readString();
        Sender = in.readString();
        Target = in.readString();
        PoliteForm = in.readString();
        Culture = in.readString();
        TagIds = in.createStringArrayList();
        AvailableCultures = in.createStringArrayList();
        Status = in.readString();
        OtherRealizationIds = in.createStringArrayList();
        TagsString = in.readString();
        RealizationIdsString = in.readString();
        CulturesString = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public void setScoring(Scoring scoring) {
        Scoring = scoring;
    }

    public Integer getNbSharesForIntention() {
        if (Scoring != null) {
            return Scoring.getNbShares();
        }
        return NbSharesForIntention;
    }

    @Override
    public Double getWeight() {
        return SortBy <= 29 ? 3.0 : 1.0;
    }

    /**
     * @return The TextId
     */
    public String getTextId() {
        return TextId;
    }

    /**
     * @param TextId The TextId
     */
    public void setTextId(String TextId) {
        this.TextId = TextId;
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
     * @return The IntentionPrototypeSlug
     */
    public String getIntentionPrototypeSlug() {
        return IntentionPrototypeSlug;
    }

    /**
     * @return The PrototypeId
     */
    public String getPrototypeId() {
        return PrototypeId;
    }

    /**
     * @param PrototypeId The PrototypeId
     */
    public void setPrototypeId(String PrototypeId) {
        this.PrototypeId = PrototypeId;
    }

    /**
     * @return The Content
     */
    public String getContent() {
        return Content;
    }

    /**
     * @param Content The Content
     */
    public void setContent(String Content) {
        this.Content = Content;
    }

    /**
     * @return The SortBy
     */
    public Integer getSortBy() {
        return SortBy;
    }

    /**
     * @param SortBy The SortBy
     */
    public void setSortBy(Integer SortBy) {
        this.SortBy = SortBy;
    }

    /**
     * @return The Sender
     */
    public String getSender() {
        return Sender;
    }

    /**
     * @param Sender The Sender
     */
    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    /**
     * @return The Target
     */
    public String getTarget() {
        return Target;
    }

    /**
     * @param Target The Target
     */
    public void setTarget(String Target) {
        this.Target = Target;
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
     * @return The TagIds
     */
    public List<String> getTagIds() {
        return TagIds;
    }

    /**
     * @param TagIds The TagIds
     */
    public void setTagIds(List<String> TagIds) {
        this.TagIds = TagIds;
    }

    /**
     * @return The AvailableCultures
     */
    public List<String> getAvailableCultures() {
        return AvailableCultures;
    }

    /**
     * @param AvailableCultures The AvailableCultures
     */
    public void setAvailableCultures(List<String> AvailableCultures) {
        this.AvailableCultures = AvailableCultures;
    }

    /**
     * @return The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param Status The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     * @return The OtherRealizationIds
     */
    public List<String> getOtherRealizationIds() {
        return OtherRealizationIds;
    }

    /**
     * @param OtherRealizationIds The OtherRealizationIds
     */
    public void setOtherRealizationIds(List<String> OtherRealizationIds) {
        this.OtherRealizationIds = OtherRealizationIds;
    }

    /**
     * @return The TagsString
     */
    public String getTagsString() {
        return TagsString;
    }

    /**
     * @param TagsString The TagsString
     */
    public void setTagsString(String TagsString) {
        this.TagsString = TagsString;
    }

    /**
     * @return The RealizationIdsString
     */
    public String getRealizationIdsString() {
        return RealizationIdsString;
    }

    /**
     * @param RealizationIdsString The RealizationIdsString
     */
    public void setRealizationIdsString(String RealizationIdsString) {
        this.RealizationIdsString = RealizationIdsString;
    }

    /**
     * @return The CulturesString
     */
    public String getCulturesString() {
        return CulturesString;
    }

    /**
     * @param CulturesString The CulturesString
     */
    public void setCulturesString(String CulturesString) {
        this.CulturesString = CulturesString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TextId);
        dest.writeString(IntentionId);
        dest.writeString(IntentionPrototypeSlug);
        dest.writeString(PrototypeId);
        dest.writeString(Content);
        dest.writeString(Sender);
        dest.writeString(Target);
        dest.writeString(PoliteForm);
        dest.writeString(Culture);
        dest.writeStringList(TagIds);
        dest.writeStringList(AvailableCultures);
        dest.writeString(Status);
        dest.writeStringList(OtherRealizationIds);
        dest.writeString(TagsString);
        dest.writeString(RealizationIdsString);
        dest.writeString(CulturesString);
    }

    public class Scoring {
        @SerializedName("NbShares")
        @Expose
        private Integer NbShares;
        @SerializedName("NbDisplays")
        @Expose
        private Integer NbDisplays;
        @SerializedName("Score")
        @Expose
        private Double Score;
        @SerializedName("Rank")
        @Expose
        private Integer Rank;
        @SerializedName("DenseRank")
        @Expose
        private Integer DenseRank;

        /**
         * @return The NbShares
         */
        public Integer getNbShares() {
            return NbShares;
        }

        /**
         * @param NbShares The NbShares
         */
        public void setNbShares(Integer NbShares) {
            this.NbShares = NbShares;
        }

        /**
         * @return The NbDisplays
         */
        public Integer getNbDisplays() {
            return NbDisplays;
        }

        /**
         * @param NbDisplays The NbDisplays
         */
        public void setNbDisplays(Integer NbDisplays) {
            this.NbDisplays = NbDisplays;
        }

        /**
         * @return The Score
         */
        public Double getScore() {
            return Score;
        }

        /**
         * @param Score The Score
         */
        public void setScore(Double Score) {
            this.Score = Score;
        }

        /**
         * @return The Rank
         */
        public Integer getRank() {
            return Rank;
        }

        /**
         * @param Rank The Rank
         */
        public void setRank(Integer Rank) {
            this.Rank = Rank;
        }

        /**
         * @return The DenseRank
         */
        public Integer getDenseRank() {
            return DenseRank;
        }

        /**
         * @param DenseRank The DenseRank
         */
        public void setDenseRank(Integer DenseRank) {
            this.DenseRank = DenseRank;
        }

    }

}
