package com.fpmislata.back.web.webModel.request;

public record InsertCategoryRequest(
    String name,
    String slug,
    String description,
    Boolean estado) {

}
