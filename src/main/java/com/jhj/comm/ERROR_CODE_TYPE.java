package com.jhj.comm;

public enum  ERROR_CODE_TYPE {

    UNKNOWN(0,"错误,请重新操作"),
    SUCCESS(1,""),
    ERROR_USERNAME_OR_PASSWORD(2,"账号密码错误"),
    ILLEGAL_PARAMETER(3,"输入的参数不合法，请再次检查"),
    ORDER_CHANGE(4,"订单状态已被修改,请重新操作"),
    ILLEGAL_TOKEN(1000,""),
    ILLEGAL_AUTH(1001,"此用户没有权限");

    private int value;
    private String info;

    ERROR_CODE_TYPE(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public String info(){
        return info;
    }

    public int intValue(){
        return value;
    }


}
