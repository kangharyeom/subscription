package dott.subscription.exception;

import lombok.Getter;

public enum Exceptions {

    // 공통 예외
    QUERY_ERROR(400, "Query Error"),
    INVALID_DATE(400, "Invalid Date"),
    INVALID_VALUES(400, "Invalid Values"),
    UNAUTHORIZED(401, "Unauthorized"),
    ACCESS_FORBIDDEN(403, "Access forbidden"),
    NOT_FOUND(404, "Not Found"),

    // Member 도메인 발생 예외
    PHONE_NUMBER_EXIST(400, "Phone number is already in use"),
    MEMBER_NOT_FOUND(404, "Member is not found"),

    // Channel 도메인 발생 예외
    CHANNEL_EXIST(400, "Channel is already exist"),
    CHANNEL_NOT_FOUND(404, "Channel is not found"),

    // Subscription 도메인 발생 예외
    SUBSCRIPTION_API_CONNECTION_ERROR(400, "Subscription Api Connection Error : Transaction rollback"),
    SUBSCRIPTION_EXIST(400, "Member is Already Subscribe"),
    SUBSCRIPTION_NOT_FOUND(404, "Unsubscribe Member"),
    SUBSCRIPTION_HISTORY_NOT_FOUND(404, "Subscription History Not Found");



    @Getter
    private int status;
    @Getter
    private String message;

    Exceptions(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
