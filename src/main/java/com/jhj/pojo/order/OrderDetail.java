package com.jhj.pojo.order;

import java.util.List;
import java.util.TreeMap;

public class OrderDetail {
    private OrderInfo orderInfo;
    List<Detail> list;


    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<Detail> getList() {
        return list;
    }

    public void setList(List<Detail> list) {
        this.list = list;
    }
}
