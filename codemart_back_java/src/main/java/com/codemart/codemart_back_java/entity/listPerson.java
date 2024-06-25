package com.codemart.codemart_back_java.entity;

import lombok.Data;

import java.util.List;

@Data
public class listPerson {

    private Integer dui_id;
    private Integer u_id;
    private String username;
    private String nickname;
    private String join_time;
    private List<String> skusList;
    private String skus;
    private String show;
    private Integer dsod_dui_id;

}
