package com.fpmislata.back.controller.webModel.request;

public record InsertCategoryRequest(
    String name,
    String slug,
    String description,
    Boolean estado
) {

}
