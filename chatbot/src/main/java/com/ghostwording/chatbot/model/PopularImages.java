package com.ghostwording.chatbot.model;

import com.ghostwording.chatbot.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PopularImages {

    @SerializedName("Images")
    @Expose
    private List<Image> Images = new ArrayList<>();
    @SerializedName("IntentionId")
    @Expose
    private String IntentionId;
    @SerializedName("IntentionLabel")
    @Expose
    private String IntentionLabel;

    /**
     * @return The Images
     */
    public List<Image> getImages() {
        return Images;
    }

    /**
     * @param Images The Images
     */
    public void setImages(List<Image> Images) {
        this.Images = Images;
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
     * @return The IntentionLabel
     */
    public String getIntentionLabel() {
        return IntentionLabel;
    }

    /**
     * @param IntentionLabel The IntentionLabel
     */
    public void setIntentionLabel(String IntentionLabel) {
        this.IntentionLabel = IntentionLabel;
    }

    public static class Image {
        @SerializedName("ImageName")
        @Expose
        private String ImageName;
        @SerializedName("ImageLink")
        @Expose
        private String ImageLink;
        @SerializedName("NbSharesForIntention")
        @Expose
        private Integer NbSharesForIntention;
        @SerializedName("NbDisplaysForIntention")
        @Expose
        private Integer NbDisplaysForIntention;
        @SerializedName("ScoreForIntention")
        @Expose
        private Double ScoreForIntention;
        @SerializedName("RankForIntention")
        @Expose
        private Integer RankForIntention;
        @SerializedName("DenseRankForIntention")
        @Expose
        private Integer DenseRankForIntention;
        @SerializedName("GlobalRank")
        @Expose
        private Integer GlobalRank;
        @SerializedName("Scoring")
        @Expose
        private Scoring Scoring;

        /**
         * @return The ImageName
         */
        public String getImageName() {
            return ImageName;
        }

        /**
         * @param ImageName The ImageName
         */
        public void setImageName(String ImageName) {
            this.ImageName = ImageName;
        }

        /**
         * @return The ImageLink
         */
        public String getImageLink() {
            if (ImageLink == null) {
                return Utils.getImagePrefix() + ImageName;
            }
            return ImageLink;
        }

        /**
         * @param ImageLink The ImageLink
         */
        public void setImageLink(String ImageLink) {
            this.ImageLink = ImageLink;
        }

        /**
         * @return The NbSharesForIntention
         */
        public Integer getNbSharesForIntention() {
            if (Scoring != null) {
                return Scoring.getNbShares();
            }
            return NbSharesForIntention;
        }

        /**
         * @param NbSharesForIntention The NbSharesForIntention
         */
        public void setNbSharesForIntention(Integer NbSharesForIntention) {
            this.NbSharesForIntention = NbSharesForIntention;
        }

        /**
         * @return The NbDisplaysForIntention
         */
        public Integer getNbDisplaysForIntention() {
            if (Scoring != null) {
                return Scoring.getNbDisplays();
            }
            return NbDisplaysForIntention;
        }

        /**
         * @param NbDisplaysForIntention The NbDisplaysForIntention
         */
        public void setNbDisplaysForIntention(Integer NbDisplaysForIntention) {
            this.NbDisplaysForIntention = NbDisplaysForIntention;
        }

        /**
         * @return The ScoreForIntention
         */
        public Double getScoreForIntention() {
            return ScoreForIntention;
        }

        /**
         * @param ScoreForIntention The ScoreForIntention
         */
        public void setScoreForIntention(Double ScoreForIntention) {
            this.ScoreForIntention = ScoreForIntention;
        }

        /**
         * @return The RankForIntention
         */
        public Integer getRankForIntention() {
            return RankForIntention;
        }

        /**
         * @param RankForIntention The RankForIntention
         */
        public void setRankForIntention(Integer RankForIntention) {
            this.RankForIntention = RankForIntention;
        }

        /**
         * @return The DenseRankForIntention
         */
        public Integer getDenseRankForIntention() {
            return DenseRankForIntention;
        }

        /**
         * @param DenseRankForIntention The DenseRankForIntention
         */
        public void setDenseRankForIntention(Integer DenseRankForIntention) {
            this.DenseRankForIntention = DenseRankForIntention;
        }

        /**
         * @return The GlobalRank
         */
        public Integer getGlobalRank() {
            return GlobalRank;
        }

        /**
         * @param GlobalRank The GlobalRank
         */
        public void setGlobalRank(Integer GlobalRank) {
            this.GlobalRank = GlobalRank;
        }

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
