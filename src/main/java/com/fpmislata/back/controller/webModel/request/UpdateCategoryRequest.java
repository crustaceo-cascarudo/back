package com.fpmislata.back.controller.webModel.request;

public record UpdateCategoryRequest(
    Long id,
    String name,
    String slug,
    String description,
    Boolean estado
) {

}
