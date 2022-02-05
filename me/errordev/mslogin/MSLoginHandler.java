package me.errordev.mslogin;

public interface MSLoginHandler {
    void onLoginSuccess(final String uuid, final String username, final String accessToken);
}