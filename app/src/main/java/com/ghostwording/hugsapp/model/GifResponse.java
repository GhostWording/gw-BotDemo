package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GifResponse {

    @SerializedName("data")
    @Expose
    private List<GifImage> data = null;

    public List<GifImage> getData() {
        return data;
    }

    public void setData(List<GifImage> data) {
        this.data = data;
    }

    public static class GifImage {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("bitly_gif_url")
        @Expose
        private String bitlyGifUrl;
        @SerializedName("bitly_url")
        @Expose
        private String bitlyUrl;
        @SerializedName("embed_url")
        @Expose
        private String embedUrl;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("content_url")
        @Expose
        private String contentUrl;
        @SerializedName("source_tld")
        @Expose
        private String sourceTld;
        @SerializedName("source_post_url")
        @Expose
        private String sourcePostUrl;
        @SerializedName("import_datetime")
        @Expose
        private String importDatetime;
        @SerializedName("trending_datetime")
        @Expose
        private String trendingDatetime;
        @SerializedName("images")
        @Expose
        private Images images;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBitlyGifUrl() {
            return bitlyGifUrl;
        }

        public void setBitlyGifUrl(String bitlyGifUrl) {
            this.bitlyGifUrl = bitlyGifUrl;
        }

        public String getBitlyUrl() {
            return bitlyUrl;
        }

        public void setBitlyUrl(String bitlyUrl) {
            this.bitlyUrl = bitlyUrl;
        }

        public String getEmbedUrl() {
            return embedUrl;
        }

        public void setEmbedUrl(String embedUrl) {
            this.embedUrl = embedUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }


        public String getSourceTld() {
            return sourceTld;
        }

        public void setSourceTld(String sourceTld) {
            this.sourceTld = sourceTld;
        }

        public String getSourcePostUrl() {
            return sourcePostUrl;
        }

        public void setSourcePostUrl(String sourcePostUrl) {
            this.sourcePostUrl = sourcePostUrl;
        }

        public String getImportDatetime() {
            return importDatetime;
        }

        public void setImportDatetime(String importDatetime) {
            this.importDatetime = importDatetime;
        }

        public String getTrendingDatetime() {
            return trendingDatetime;
        }

        public void setTrendingDatetime(String trendingDatetime) {
            this.trendingDatetime = trendingDatetime;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }
    }

    public static class FixedHeight {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("mp4")
        @Expose
        private String mp4;
        @SerializedName("mp4_size")
        @Expose
        private String mp4Size;
        @SerializedName("webp")
        @Expose
        private String webp;
        @SerializedName("webp_size")
        @Expose
        private String webpSize;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }

        public String getMp4Size() {
            return mp4Size;
        }

        public void setMp4Size(String mp4Size) {
            this.mp4Size = mp4Size;
        }

        public String getWebp() {
            return webp;
        }

        public void setWebp(String webp) {
            this.webp = webp;
        }

        public String getWebpSize() {
            return webpSize;
        }

        public void setWebpSize(String webpSize) {
            this.webpSize = webpSize;
        }
    }

    public class FixedWidth {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("mp4")
        @Expose
        private String mp4;
        @SerializedName("mp4_size")
        @Expose
        private String mp4Size;
        @SerializedName("webp")
        @Expose
        private String webp;
        @SerializedName("webp_size")
        @Expose
        private String webpSize;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }

        public String getMp4Size() {
            return mp4Size;
        }

        public void setMp4Size(String mp4Size) {
            this.mp4Size = mp4Size;
        }

        public String getWebp() {
            return webp;
        }

        public void setWebp(String webp) {
            this.webp = webp;
        }

        public String getWebpSize() {
            return webpSize;
        }

        public void setWebpSize(String webpSize) {
            this.webpSize = webpSize;
        }

    }

    public class Original {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("size")
        @Expose
        private String size;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    public static class Images {

        @SerializedName("fixed_height")
        @Expose
        private FixedHeight fixedHeight;
        @SerializedName("fixed_width")
        @Expose
        private FixedWidth fixedWidth;
        @SerializedName("original")
        @Expose
        private Original original;

        public Original getOriginal() {
            return original;
        }

        public FixedHeight getFixedHeight() {
            return fixedHeight;
        }

        public void setFixedHeight(FixedHeight fixedHeight) {
            this.fixedHeight = fixedHeight;
        }

        public FixedWidth getFixedWidth() {
            return fixedWidth;
        }

        public void setFixedWidth(FixedWidth fixedWidth) {
            this.fixedWidth = fixedWidth;
        }

    }

}


