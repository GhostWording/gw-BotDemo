package com.ghostwording.chatbot.model.texts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizContent {

    @SerializedName("Text")
    @Expose
    private Quote Text;
    @SerializedName("ImageUrl")
    @Expose
    private ImageUrl ImageUrl;

    /**
     * @return The Text
     */
    public Quote getText() {
        return Text;
    }

    /**
     * @param Text The Text
     */
    public void setText(Quote Text) {
        this.Text = Text;
    }

    /**
     * @return The ImageUrl
     */
    public ImageUrl getImageUrl() {
        return ImageUrl;
    }

    /**
     * @param ImageUrl The ImageUrl
     */
    public void setImageUrl(ImageUrl ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public class ImageUrl {
        @SerializedName("Url")
        @Expose
        private String Url;

        /**
         * @return The Url
         */
        public String getUrl() {
            return Url;
        }

        /**
         * @param Url The Url
         */
        public void setUrl(String Url) {
            this.Url = Url;
        }
    }
}
