package com.fpmislata.back.web.webModel.request;

public record LoginUserRequest(
    String email,
    String plainPassword) {

}
