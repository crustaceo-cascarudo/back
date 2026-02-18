package com.fpmislata.back.web.webModel.request;

public record UpdateCategoryRequest(
    Long id,
    String name,
    String slug,
    String description,
    Boolean estado) {

}
