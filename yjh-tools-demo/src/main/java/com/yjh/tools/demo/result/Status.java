package com.yjh.tools.demo.result;

/**
 * 状态码
 */
public enum Status {

    SUCCESS(1, "操作成功"),
    ERROR(-1, "程序开了个小差"),
    ;

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
