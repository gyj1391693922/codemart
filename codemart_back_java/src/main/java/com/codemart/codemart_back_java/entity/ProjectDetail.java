package com.codemart.codemart_back_java.entity;

import lombok.Data;

@Data
public class ProjectDetail {

    private Integer id;
    private String title;
    private String sku;
    private String money;
    private Integer author_id;
    private String nickname;
    private String term_day;
    private String context;
    private String status;
    private Integer plan_id;
    private Boolean pageStatus;
    private String create_time;

}
