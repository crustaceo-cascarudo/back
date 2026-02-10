package com.fpmislata.back.web.webModel.response;

public record LoginResponse(
    String token,
    UserResponse user) {

}
