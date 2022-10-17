package com.oracle.ticketing.exception;

public class TicketingException extends Exception {

    private String errorCode;
    private String errorMessage;

    public TicketingException(String errorMessage){
        super(errorMessage);

    }

    public TicketingException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode= errorCode;
        this.errorMessage= errorMessage;

    }
    public TicketingException(String errorCode, String errorMessage, Throwable e){
        super(errorMessage, e);
        this.errorCode= errorCode;
        this.errorMessage= errorMessage;

    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
