package com.jhj.comm;

public class OrderState {
    public static final int INIT=0;  //初始化
    public static final int PUBLISH=1; //采购方发布 ->等待供应方确认
    public static final int CONFIRM=2; //供应商确认 -> 供应商创建订单细节
    public static final int DETAIL=3; //供应商创建订单细节 -> 采购方确认
    public static final int CREATED=4;  //采购方确认 订单成立
    public static final int FINISH=5;  //完成状态
}
