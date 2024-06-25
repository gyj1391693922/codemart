package com.codemart.codemart_back_java.common;

public class R<T> {

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R() {
    }

    public R(T data) {
        this.data = data;
    }

    public static R success() {
        R result = new R<>();
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    public static <T> R<T> success(T data) {
        R<T> result = new R<>(data);
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    public static <T> R<T> success(T data,String msg) {
        R<T> result = new R<>(data);
        result.setCode("200");
        result.setMsg(msg);
        return result;
    }

    public static R error(String code, String msg) {
        R result = new R();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static R error(String msg){
        R result = new R();
        result.setCode("500");
        result.setMsg(msg);
        return result;
    }

}
