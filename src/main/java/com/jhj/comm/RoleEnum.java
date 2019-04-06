package com.jhj.comm;

public enum  RoleEnum {
    NULL(0,"null"),BUYER(1,"buyer"),SUPPLIER(2,"supplier");

    private int iRole;
    private String sRole;
    RoleEnum(int iRole, String sRole) {
        this.iRole=iRole;
        this.sRole=sRole;
    }

    public int intValue() {
        return iRole;
    }

    public String strValue() {
        return sRole;
    }
}
