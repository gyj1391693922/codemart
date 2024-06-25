package com.codemart.codemart_back_java.entity;

import lombok.Data;

import java.util.List;

@Data
public class ProjectSaveInfo {

    private Integer deId;
    private Integer authorId;
    private String title;
    private List<Integer> sku;
    private String context;
    private Double money;
    private Integer days;

}
