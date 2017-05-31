package com.application.social.utils;

/**
 * Created by Ridhi on 3/26/2017.
 */

public interface AfterUpload {
    public void doneLoggingIn();

    void doneLoggingIn(String message);

    void doneLoggingOut(String message);

    void doneTwitterLogIn();

    void donePinterestLogIn();

    void doneFacebookLogIn();

    void doneInstagramLogIn();

    void doneTwitterPost();
}
