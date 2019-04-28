package com.jhj.dao;

import com.jhj.pojo.order.Detail;
import com.jhj.pojo.order.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    Long createOrder(OrderInfo orderInfo);

    List<Long> getAllOrderID(Long uuid,boolean bBuyer,Long supplierId,Integer orderType);

    List<Long> getAllEndOrderID(Long uuid, boolean bBuyer, Long supplierId);

    List<OrderInfo> getOrderByIds(List<Long> targerIds);

    int updateOrderStatus(long orderId, int confirm);

    OrderInfo getOrder(Long orderId);

    int updateOrder(OrderInfo orderInfo);

    int deleteOrder(Long orderId);

    List<Detail> getOrderDetails(Long orderId, int limit, int page);

    List<Detail> getAllOrderDetails(Long orderId);

    boolean createOrderDetails(long orderId,List<Detail> list);

    Long getOrderDetailCount(Long orderId);

    Detail getOrderDetail(long detailId);

    void setOrderDetailState(long detailId);

    boolean deleteAllOrderDetail(long orderId);

    boolean deleteOrderDetail(long orderId);

    void deleteOrderBySupplier(long supplierid,long buyid);

}
