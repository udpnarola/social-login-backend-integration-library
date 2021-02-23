package com.github.udpnarola.constant;

public class ErrorMessage {

    public static final String ERR_GET_TWITTER_REQUEST_TOKEN = "Unable to get request token from Twitter";
    public static final String ERR_GET_TWITTER_USER_DETAIL = "Unable to get Twitter user details";
    public static final String ERR_BUILD_URI = "Unable to build URI, please check your client-id and redirect-uri syntax";
    public static final String ERR_EMPTY_OR_NULL_AUTH_CODE = "The auth code can not be empty or null";
    public static final String ERR_GET_GOOGLE_USER_DETAIL = "Unable to get Google user details";
    public static final String ERR_GET_LINKEDIN_USER_DETAIL = "Unable to get Linkedin user details";
    public static final String ERR_GET_LINKEDIN_USER_EMAIL = "Unable to get Linkedin user email";

    private ErrorMessage() {
    }
}
