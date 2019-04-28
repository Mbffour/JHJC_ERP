package com.jhj.service.impl;

import com.jhj.comm.OrderState;
import com.jhj.comm.PageModel;
import com.jhj.comm.RoleEnum;
import com.jhj.comm.Utils;
import com.jhj.dao.OrderDao;
import com.jhj.dao.UserDao;
import com.jhj.pojo.order.Detail;
import com.jhj.pojo.order.OrderDetail;
import com.jhj.pojo.order.OrderInfo;
import com.jhj.pojo.user.UserInfo;
import com.jhj.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    UserDao userDao;
    //1 创建订单
    @Override
    public OrderInfo createOrder(OrderInfo orderInfo) {
        //1 权限验证 TODO

        //2 创建订单
        orderInfo.setOrderStat(OrderState.INIT);
        Long orderid = orderDao.createOrder(orderInfo);

        if(orderid!=null&&orderid>0){
            orderInfo.setOrderId(orderid);
            //供应商名字
            UserInfo user = userDao.getUser(orderInfo.getSupplierID());
            orderInfo.setSupplierName(user.getName());
            return orderInfo;
        }
        return null;
    }

    //2 供应方确认订单 填写具体发货进度
    @Override
    public void supplierAck(Long orderId) {

        //1 权限 TODO

        //2 获取订单信息修改状态  TODO
        OrderInfo orderInfo = getOrderInfo(orderId);
        if(orderInfo.getOrderStat()!=OrderState.INIT){
            //错误
            return;
        }


        orderInfo.setOrderStat(OrderState.CONFIRM);

        //3 创建订单细节
        OrderDetail detail = new OrderDetail();

        //detail.add(20190203,11,"");
        //4 入库 TODO

    }


    @Override
    public void buyerAck(Long orderId) {

        //1 权限 TODO

        //2 获取订单信息修改状态  TODO
        OrderInfo orderInfo = getOrderInfo(orderId);

        //是否同意 不同意订单不成立
        orderInfo.setOrderStat(OrderState.CREATED);

        //3 入库修改

    }

    @Override
    public void buyerConfirm(Long orderId) {
        //1 权限

        //2 确认收货 订单细节表
       // OrderDetail orderDetail = getOrderDetail(orderId);

        //orderDetail.


    }

    //筛选
    @Override
    public OrderInfo getAllOrderInfo() {
        //1 权限 获取用户
        //2 获取用户的所有订单


        return null;
    }

    @Override
    public PageModel getOrderByPage(Long uuid, int limit, int page,boolean bBuyer,Long supplierId,Integer orderType) {

        //计算
        int start = limit*(page-1);

        //1 查所有userid

        List<Long> allIds = null;
        if(orderType!=null){
            allIds = orderDao.getAllEndOrderID(uuid,bBuyer,supplierId);
        }else{
            allIds = orderDao.getAllOrderID(uuid,bBuyer,supplierId,orderType);
        }

        if(allIds==null&&allIds.size()==0)
            return null;


        List<Long> targerIds = new ArrayList<>();

        //2 分页
        for(int i=0;i<allIds.size();i++){

            if(i>=start&&i<(start+limit)){
                targerIds.add(allIds.get(i));
            }
        }

        if(targerIds!=null&&targerIds.size()>0){

            List<OrderInfo> orderByIds = orderDao.getOrderByIds(targerIds);
            //供应商 填充买家名称
            if(!bBuyer&&orderByIds!=null&&orderByIds.size()>0){
                HashMap<Long, String> map = new HashMap<>();
                for(OrderInfo info:orderByIds){
                    if(map.containsKey(info.getBuyerID())){
                        info.setBuyerName(map.get(info.getBuyerID()));
                    }else{
                        //调用数据库
                        UserInfo user = userDao.getUser(info.getBuyerID());
                        info.setBuyerName(user.getName());
                        map.put(uuid,user.getName());
                    }
                }
            }

            return PageModel.genData(orderByIds,allIds.size());
        }

        return  null;


    }

    @Override
    public boolean setOrderStatus(long orderId, int confirm) {
        return  orderDao.updateOrderStatus(orderId,confirm)>0?true:false;
    }

    @Override
    public boolean updateOrder(OrderInfo orderInfo) {
        return orderDao.updateOrder(orderInfo)>0?true:false;
    }

    @Override
    public boolean deleteOrder(Long orderId) {

        try{
            orderDao.deleteOrder(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean createOrderDetail(long orderId, List<Detail> list) {

         if(orderDao.createOrderDetails(orderId, list)){
             orderDao.updateOrderStatus(orderId,OrderState.DETAIL);
             return true;
         }

        return false;
    }

    @Override
    public Detail getOrderDetail(long orderId, long detailId) {

        return  orderDao.getOrderDetail(detailId);
    }

    @Override
    public boolean confirmDetail(long detailId) {


        try{
            orderDao.setOrderDetailState(detailId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Detail> getAllOrderDetail(long orderId) {


        return orderDao.getAllOrderDetails(orderId);
    }

    @Override
    public boolean updateOrderDetails(long orderId, List<Detail> updateList) {

        boolean b= orderDao.deleteAllOrderDetail(orderId);
        if(orderDao.createOrderDetails(orderId, updateList)){
            return true;
        }

        return false;
    }

    @Override
    public void deleteOrderBySupplier(long supplierid,long buyid) {
        orderDao.deleteOrderBySupplier(supplierid,buyid);
    }

    @Override
    public void deleteOrderDetail(long orderId) {
        orderDao.deleteOrderDetail(orderId);
    }

    /**
     * 直接查库
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderDao.getOrder(orderId);
    }

    // 获取某订单的所有细节
    @Override
    public PageModel getOrderDetailByPage(Long orderId,int limit, int page) {

        OrderInfo order = orderDao.getOrder(orderId);
        if(order==null)return null;
        UserInfo user = userDao.getUser(order.getBuyerID());
        if(user!=null)order.setBuyerName(user.getName());


        //计算
        int start = limit*(page-1);

        Long num = orderDao.getOrderDetailCount(orderId);

        if(num==0)return null;
        //1 查所有userid
        List<Detail> details = orderDao.getOrderDetails(orderId,start,limit);

        return PageModel.genData(details,num.intValue(),order);
    }


}
