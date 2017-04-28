package com.application.social.utils.Instagram;

public interface InstagramListener {

  void onInstagramSignInFail(String errorMessage);

  void onInstagramSignInSuccess(String authToken, String userId);
}
