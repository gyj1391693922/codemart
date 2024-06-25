package com.codemart.codemart_back_java.exceptions;

public class ServiceException extends RuntimeException{

    private String code = "500";

    public String getCode(){return code;}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String code,String message){
        super(message);
        this.code = code;
    }
}
