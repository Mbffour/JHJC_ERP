package com.jhj.pojo.order;

public class Detail {
    private long orderId;
    private long time;
    private int expectNum; //预期交付数量
    private int actualNum; //实际交付数量
    private String remarks; //备注
    private int state;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Detail(int expectNum, int actualNum, long time, long orderid) {
        this.expectNum = expectNum;
        this.actualNum = actualNum;
        this.time = time;
        this.orderId = orderid;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getExpectNum() {
        return expectNum;
    }

    public void setExpectNum(int expectNum) {
        this.expectNum = expectNum;
    }

    public int getActualNum() {
        return actualNum;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Override
    public String toString() {
        return "Detail{" +
                "orderId=" + orderId +
                ", time=" + time +
                ", expectNum=" + expectNum +
                ", actualNum=" + actualNum +
                ", remarks='" + remarks + '\'' +
                ", state=" + state +
                ", id=" + id +
                '}';
    }
}
