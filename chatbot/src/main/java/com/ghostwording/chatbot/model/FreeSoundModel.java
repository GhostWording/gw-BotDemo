package com.ghostwording.chatbot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FreeSoundModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("geotag")
    @Expose
    private Object geotag;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("channels")
    @Expose
    private Integer channels;
    @SerializedName("filesize")
    @Expose
    private Integer filesize;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;
    @SerializedName("bitdepth")
    @Expose
    private Integer bitdepth;
    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("samplerate")
    @Expose
    private Integer samplerate;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("pack")
    @Expose
    private String pack;
    @SerializedName("pack_name")
    @Expose
    private String packName;
    @SerializedName("download")
    @Expose
    private String download;
    @SerializedName("bookmark")
    @Expose
    private String bookmark;
    @SerializedName("previews")
    @Expose
    private Previews previews;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("num_downloads")
    @Expose
    private Integer numDownloads;
    @SerializedName("avg_rating")
    @Expose
    private Double avgRating;
    @SerializedName("num_ratings")
    @Expose
    private Integer numRatings;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("num_comments")
    @Expose
    private Integer numComments;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("similar_sounds")
    @Expose
    private String similarSounds;
    @SerializedName("analysis")
    @Expose
    private String analysis;
    @SerializedName("analysis_frames")
    @Expose
    private String analysisFrames;
    @SerializedName("analysis_stats")
    @Expose
    private String analysisStats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getGeotag() {
        return geotag;
    }

    public void setGeotag(Object geotag) {
        this.geotag = geotag;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getBitdepth() {
        return bitdepth;
    }

    public void setBitdepth(Integer bitdepth) {
        this.bitdepth = bitdepth;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getSamplerate() {
        return samplerate;
    }

    public void setSamplerate(Integer samplerate) {
        this.samplerate = samplerate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public Previews getPreviews() {
        return previews;
    }

    public void setPreviews(Previews previews) {
        this.previews = previews;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Integer getNumDownloads() {
        return numDownloads;
    }

    public void setNumDownloads(Integer numDownloads) {
        this.numDownloads = numDownloads;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSimilarSounds() {
        return similarSounds;
    }

    public void setSimilarSounds(String similarSounds) {
        this.similarSounds = similarSounds;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnalysisFrames() {
        return analysisFrames;
    }

    public void setAnalysisFrames(String analysisFrames) {
        this.analysisFrames = analysisFrames;
    }

    public String getAnalysisStats() {
        return analysisStats;
    }

    public void setAnalysisStats(String analysisStats) {
        this.analysisStats = analysisStats;
    }

    public class Images {

        @SerializedName("waveform_l")
        @Expose
        private String waveformL;
        @SerializedName("waveform_m")
        @Expose
        private String waveformM;
        @SerializedName("spectral_m")
        @Expose
        private String spectralM;
        @SerializedName("spectral_l")
        @Expose
        private String spectralL;

        public String getWaveformL() {
            return waveformL;
        }

        public void setWaveformL(String waveformL) {
            this.waveformL = waveformL;
        }

        public String getWaveformM() {
            return waveformM;
        }

        public void setWaveformM(String waveformM) {
            this.waveformM = waveformM;
        }

        public String getSpectralM() {
            return spectralM;
        }

        public void setSpectralM(String spectralM) {
            this.spectralM = spectralM;
        }

        public String getSpectralL() {
            return spectralL;
        }

        public void setSpectralL(String spectralL) {
            this.spectralL = spectralL;
        }
    }

    public class Previews {

        @SerializedName("preview-lq-ogg")
        @Expose
        private String previewLqOgg;
        @SerializedName("preview-lq-mp3")
        @Expose
        private String previewLqMp3;
        @SerializedName("preview-hq-ogg")
        @Expose
        private String previewHqOgg;
        @SerializedName("preview-hq-mp3")
        @Expose
        private String previewHqMp3;

        public String getPreviewLqOgg() {
            return previewLqOgg;
        }

        public void setPreviewLqOgg(String previewLqOgg) {
            this.previewLqOgg = previewLqOgg;
        }

        public String getPreviewLqMp3() {
            return previewLqMp3;
        }

        public void setPreviewLqMp3(String previewLqMp3) {
            this.previewLqMp3 = previewLqMp3;
        }

        public String getPreviewHqOgg() {
            return previewHqOgg;
        }

        public void setPreviewHqOgg(String previewHqOgg) {
            this.previewHqOgg = previewHqOgg;
        }

        public String getPreviewHqMp3() {
            return previewHqMp3;
        }

        public void setPreviewHqMp3(String previewHqMp3) {
            this.previewHqMp3 = previewHqMp3;
        }

    }

}
