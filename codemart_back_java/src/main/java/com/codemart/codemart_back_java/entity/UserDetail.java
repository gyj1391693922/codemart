package com.codemart.codemart_back_java.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserDetail {

    String username;
    String nickname;
    String phone;
    String birthday;
    String sku;
    List<String> skuList;
    String address;

}
