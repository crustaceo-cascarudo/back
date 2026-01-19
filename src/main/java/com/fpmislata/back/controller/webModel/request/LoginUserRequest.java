package com.fpmislata.back.controller.webModel.request;

public record LoginUserRequest(
    String email,
    String plainPassword) {

}
