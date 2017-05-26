package com.application.social.utils.Facebook;

/**
 * Created by Ridhi on 5/25/2017.
 */

public class FbVersion {
    //    private String version_name;
    private String image_url;
    private String message;
    private String story;
    private String likes;
    private String comments;
    private String profile_url;
    private String poster;
    private String has_liked;

    public String getHas_liked() {
        return has_liked;
    }

    public void setHas_liked(String has_liked) {
        this.has_liked = has_liked;
    }

    public FbVersion(){
        likes = "0";
        comments = "0";
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

//    }
//
//    public void setVersion_name(String version_name) {
//        this.version_name = version_name;
//    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
