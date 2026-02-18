package com.fpmislata.back.web.exception;

public class ErrorMessage {

  private final String error;
  private final String message;

  public ErrorMessage(Exception exception) {
    this.error = exception.getClass().getSimpleName();
    this.message = exception.getMessage();
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }
}
