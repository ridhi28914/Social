package com.application.social.data;

/**
 * Created by Ridhi on 4/6/2017.
 */

public class UserDetails {

    public String name;
    public String email;
    public String token;
    public String userId;
    public String profilePic;
    public int source;
    public String fbGoId;
    public String facebookData;
    public String fbPermission;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String password;

    public String getFacebookData() {
        return facebookData;
    }

    public void setFacebookData(String facebookData) {
        this.facebookData = facebookData;
    }

    public String getFbPermission() {
        return fbPermission;
    }

    public void setFbPermission(String fbPermission) {
        this.fbPermission = fbPermission;
    }


    public void setSource(int source) {
        this.source = source;
    }

    public String getFbGoId() {
        return fbGoId;
    }

    public void setFbGoId(String fbGoId) {
        this.fbGoId = fbGoId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getSource() {
        return source;
    }

//    public int setSource(String source) {
//        this.source = source;
//    }
}
