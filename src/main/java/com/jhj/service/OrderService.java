package com.jhj.service;


import com.jhj.comm.PageModel;
import com.jhj.pojo.order.Detail;
import com.jhj.pojo.order.OrderDetail;
import com.jhj.pojo.order.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderService {

        //0 初始化
        //1 创建 （需要供应方确认）
        //2 供应方确认  (此时供应方填入订单细节)
        //3 订购方确认  (订单成立)
        //4 进行中 （供应方发货就修改订单细节状态 采购方确认收货）
        //5 玩成的订单

    //1 创建订单
    public OrderInfo createOrder( OrderInfo orderInfo);

    //2 供应方确认订单 填写具体发货进度
    public void supplierAck(Long orderId);

    //3订购方确认订单
    public void buyerAck(Long orderId);

    //4 订购方确认收货 订单细节
    public void buyerConfirm(Long orderId);

    //5 获取订单
    public OrderInfo getOrderInfo(Long orderId);

    //6 获取订单详情
    public PageModel<Detail> getOrderDetailByPage(Long orderId,int limit, int page);


    public OrderInfo getAllOrderInfo();


    //分页订单
    PageModel getOrderByPage(Long uuid, int limit, int page,boolean bBuyer,Long supplierId);

    boolean setOrderStatus(long orderId, int confirm);

    boolean updateOrder(OrderInfo orderInfo);

    boolean deleteOrder(Long orderId);

    boolean createOrderDetail(long orderId, List<Detail> list );

    Detail getOrderDetail(long orderId,long detailId);

    boolean confirmDetail(long detailId);

    List<Detail> getAllOrderDetail(long orderId);

    boolean updateOrderDetails(long orderId, List<Detail> updateList);

    void deleteOrderBySupplier(long supplierid,long buyid);

}
