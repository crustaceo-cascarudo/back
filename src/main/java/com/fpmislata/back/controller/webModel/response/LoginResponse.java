package com.fpmislata.back.controller.webModel.response;

public record LoginResponse(
    String token,
    UserResponse user
) {

}
