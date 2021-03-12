package com.yjh.tools.demo.result;

import lombok.Data;

@Data
public class Result<T> {

    private Integer status;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        return result(Status.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return result(Status.SUCCESS, data);
    }

    public static <T> Result<T> success(String message) {
        return result(Status.SUCCESS, message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return result(Status.SUCCESS, message, data);
    }

    public static <T> Result<T> failure() {
        return result(Status.ERROR);
    }

    public static <T> Result<T> failure(String message) {
        return result(Status.ERROR, message, null);
    }

    public static <T> Result<T> result(Status status) {
        return result(status, status.getMsg());
    }

    public static <T> Result<T> result(Status status, String message) {
        return result(status, message, null);
    }

    public static <T> Result<T> result(Status status, T data) {
        return result(status, status.getMsg(), data);
    }

    public static <T> Result<T> result(Status status, String message, T data) {
        Result<T> result = new Result<>();
        result.setStatus(status.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 判断结果是否为Success
     * */
    public boolean checkSuccess() {
        return Status.SUCCESS.getCode().equals(status);
    }

}
