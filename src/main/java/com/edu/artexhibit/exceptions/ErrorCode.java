package com.edu.artexhibit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(1000, Constants.BAD_REQUEST),
    EVENT_NOT_FOUND(1001, Constants.EVENT_NOT_FOUND),
    CATEGORY_NOT_FOUND(1002, Constants.CATEGORY_NOT_FOUND),
    ARTIST_CATEGORY_NOT_FOUND(1002, Constants.ARTIST_CATEGORY_NOT_FOUND),
    USER_NOT_FOUND(1003, Constants.USER_NOT_FOUND),
    PORTFOLIO_NOT_FOUND(1004, Constants.PORTFOLIO_NOT_FOUND),
    ONLY_ONE_PORTFOLIO_ALLOWED(1004, Constants.ONLY_ONE_PORTFOLIO_ALLOWED),
    USER_ALREADY_REGISTERED(1005, Constants.USER_ALREADY_REGISTERED),
    PASSWORDS_NOT_MATCHED(1012, Constants.PASSWORDS_NOT_MATCHED),
    OLD_PASSWORD_WRONG(1013, Constants.OLD_PASSWORD_WRONG),
    CATEGORY_ALREADY_REGISTERED(1006, Constants.CATEGORY_ALREADY_REGISTERED),
    EVENT_ALREADY_REGISTERED(1007, Constants.EVENT_ALREADY_REGISTERED),
    PORTFOLIO_ALREADY_REGISTERED(1008, Constants.PORTFOLIO_ALREADY_REGISTERED),
    FORBIDDEN(1009, Constants.FORBIDDEN),
    USERNAME_OR_PASSWORD_INCORRECT(1010, Constants.USERNAME_OR_PASSWORD_INCORRECT),
    NOTIFICATION_NOT_FOUND(1011, Constants.NOTIFICATION_NOT_FOUND),
    COMMENT_NOT_FOUND(1011, Constants.COMMENT_NOT_FOUND),
    ARTIST_NOT_FOUND(1012, Constants.ARTIST_NOT_FOUND),
    ARTIST_NOT_ALLOWED_FOR_COLLECTIONS(1013, Constants.ARTIST_NOT_ALLOWED_FOR_COLLECTIONS),
    ARTIST_COLLECTIONS_NOT_FOUND(1014, Constants.ARTIST_COLLECTIONS_NOT_FOUND);
    private final int code;
    private final String msg;

    public static class Constants {
        public final static String BAD_REQUEST = "Bad request! Please provide correct data!";
        public final static String EVENT_NOT_FOUND = "Event not found!";
        public final static String CATEGORY_NOT_FOUND = "Category not found!";
        public final static String OLD_PASSWORD_WRONG = "Old password is not correct!";
        public final static String NOTIFICATION_NOT_FOUND = "Notification not found!";
        public final static String COMMENT_NOT_FOUND = "Comment not found!";
        public final static String ARTIST_CATEGORY_NOT_FOUND = "Category of the artist not found!";
        public final static String USER_NOT_FOUND = "User not found!";
        public final static String PASSWORDS_NOT_MATCHED = "New password and confirm password does not match! ";
        public final static String PORTFOLIO_NOT_FOUND = "Portfolio not found!";
        public final static String USER_ALREADY_REGISTERED = "User is already registered!";
        public final static String CATEGORY_ALREADY_REGISTERED = "Category is already registered!";
        public final static String EVENT_ALREADY_REGISTERED = "Event is already registered!";
        public final static String PORTFOLIO_ALREADY_REGISTERED = "Portfolio is already registered!";
        public final static String ONLY_ONE_PORTFOLIO_ALLOWED = "Only one portfolio is allowed to create!";
        public final static String FORBIDDEN = "Forbidden!";
        public final static String USERNAME_OR_PASSWORD_INCORRECT = "Username or password is incorrect!";
        public final static String ARTIST_NOT_FOUND = "Artist not found";
        public final static String ARTIST_NOT_ALLOWED_FOR_COLLECTIONS = "Artist not allowed for collections";
        public final static String ARTIST_COLLECTIONS_NOT_FOUND = "No collections found for this artist";
    }
}