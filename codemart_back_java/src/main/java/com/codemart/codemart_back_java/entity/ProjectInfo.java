package com.codemart.codemart_back_java.entity;

import lombok.Data;

import java.util.List;

@Data
public class ProjectInfo {

    private Integer de_id;
    private String title;
    private String sku;
    private List<String> skuList;
    private Integer person;
    private String context;
    private String create_time;
    private Long term_day;

}
