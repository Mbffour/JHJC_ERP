package com.jhj.pojo.order;


import com.jhj.pojo.user.Buyer;
import com.jhj.pojo.user.Supplier;

public class OrderInfo {
    //0 初始化
    //1 创建 （需要供应方确认）
    //2 供应方确认  (此时供应方填入订单细节)
    //3 订购方确认  (订单成立)
    //4 进行中 （供应方发货就修改订单细节状态 采购方确认收货）
    //5 玩成的订单
    private int orderStat=0; //订单状态
    private long orderId; //订单id
    private String goodsName; //商品名
    private String orderNumber; //订单号
    private Long startTime; //下单时间
    private Long endTime; //交货时间
    private Long needNum; // 下单数量
    private Long undoNum; // 未完成数量
    private String unit; //计量单位
    private Long supplierID; //供应商id
    private Long buyerID;
    private String buyerName;
    private String supplierName; //供应商id
    private OrderDetail details;  // 订单细节


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderStat=" + orderStat +
                ", orderId=" + orderId +
                ", goodsName='" + goodsName + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", needNum=" + needNum +
                ", undoNum=" + undoNum +
                ", unit='" + unit + '\'' +
                ", supplierID=" + supplierID +
                ", buyerID=" + buyerID +
                ", buyerName='" + buyerName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", details=" + details +
                '}';
    }

    public Long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Long supplierID) {
        this.supplierID = supplierID;
    }

    public Long getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(Long buyerID) {
        this.buyerID = buyerID;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getOrderStat() {
        return orderStat;
    }

    public void setOrderStat(int orderStat) {
        this.orderStat = orderStat;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }



    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setNeedNum(Long needNum) {
        this.needNum = needNum;
    }

    public void setUndoNum(Long undoNum) {
        this.undoNum = undoNum;
    }

    public long getNeedNum() {
        return needNum;
    }

    public void setNeedNum(long needNum) {
        this.needNum = needNum;
    }

    public long getUndoNum() {
        return undoNum;
    }

    public void setUndoNum(long undoNum) {
        this.undoNum = undoNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }



    public OrderDetail getDetails() {
        return details;
    }

    public void setDetails(OrderDetail details) {
        this.details = details;
    }
}
