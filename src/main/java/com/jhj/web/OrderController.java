package com.jhj.web;

import com.jhj.cash.CacheManager;
import com.jhj.comm.*;
import com.jhj.interceptors.ResponseUtil;
import com.jhj.pojo.order.Detail;
import com.jhj.pojo.order.OrderDetail;
import com.jhj.pojo.order.OrderInfo;
import com.jhj.pojo.user.UserInfo;
import com.jhj.service.OrderService;
import com.jhj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;




    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    @ResponseBody
    public Map publish( @RequestBody Map<String, String> map,HttpServletRequest request){
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);

        long orderId=0;
        int type=0;
        try{
            orderId = Long.parseLong(map.get("orderId"));
            type = Integer.parseInt(map.get("type"));

            if(type!=0&&type!=1)throw new RuntimeException("ERROR_TYPE");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

        OrderInfo orderInfo = orderService.getOrderInfo(orderId);

        if(orderInfo==null||orderInfo.getBuyerID()!=user.getUuid())
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);


        //两种状态
        if((orderInfo.getOrderStat()!=OrderState.INIT&&orderInfo.getOrderStat()!=OrderState.PUBLISH))
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ORDER_CHANGE);

        int state = type==0?OrderState.INIT:OrderState.PUBLISH;

        if(orderService.setOrderStatus(orderId,state )){
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,state);
        }

        return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);
    }


    @RequestMapping(value = "/get/page",method = RequestMethod.POST)
    @ResponseBody
    public Map page(@RequestBody Map<String, String> map, HttpServletRequest request){
        String token = request.getHeader("Token");

        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);


        int page,limit;
        Long type =null;
        try{
            page = Integer.parseInt(map.get("page"));
            limit = Integer.parseInt(map.get("limit"));
            String stype = map.get("type");
            if(stype!=null&&!stype.equals("")){
                type=Long.parseLong(stype);
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }


        boolean bBuyer =false;

        if(Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)) bBuyer =true;


        PageModel data = orderService.getOrderByPage(user.getUuid(),limit,page,bBuyer,type);



        return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,data);
    }


    @RequestMapping(value = "/getDetail/page",method = RequestMethod.POST)
    @ResponseBody
    public Map getDetail(@RequestBody Map<String, String> map, HttpServletRequest request){
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);

        for(Map.Entry<String,String> e:map.entrySet()){
            System.out.println("key:"+e.getKey()+"|| value:"+e.getValue());
        }


        int page,limit;
        long orderId;

        try{
            page = Integer.parseInt(map.get("page"));
            limit = Integer.parseInt(map.get("limit"));
            orderId = Long.parseLong(map.get("orderId"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

        PageModel<Detail> data = orderService.getOrderDetailByPage(orderId,limit,page);

        return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,data);
    }


    @RequestMapping(value = "/buyer/update",method = RequestMethod.POST)
    @ResponseBody
    public Map buyerUpdate(@RequestBody Map<String, String> map, HttpServletRequest request){

        for(Map.Entry<String,String> e:map.entrySet()){
            System.out.println("key:"+e.getKey()+"|| value:"+e.getValue());
        }
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);


        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER))return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);


        //参数校验
            String remark, orderNumber, goodsname;
            int status;
            Long endTime, needNum, supplierID,orderId;

            try {
                orderNumber = map.get("orderNumber");
                goodsname = map.get("goodsName");
                remark = map.get("remark");
                orderId = Long.parseLong(map.get("orderId"));


                supplierID = Long.parseLong(map.get("type"));
                endTime = Long.parseLong(map.get("endTime"));
                needNum = Long.parseLong(map.get("needNum"));
                status = Integer.parseInt(map.get("orderStat"));
                System.out.println("timestamp:" + endTime + "|| status" + status);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setOrderStat(0);
            orderInfo.setBuyerID(user.getUuid());
            orderInfo.setSupplierID(supplierID);
            orderInfo.setGoodsName(goodsname);
            orderInfo.setStartTime(System.currentTimeMillis());
            orderInfo.setEndTime(endTime);
            orderInfo.setBuyerName(user.getName());
            orderInfo.setNeedNum(needNum);
            orderInfo.setUndoNum(needNum);
            orderInfo.setOrderNumber(orderNumber);

          if(orderService.updateOrder(orderInfo)){
              return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,orderInfo);
          }

          return  ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);


    }




    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Map delete(@RequestBody Map<String, String> map, HttpServletRequest request){
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);

        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);

        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)) return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);


        Long orderId;
        try{
            orderId = Long.parseLong(map.get("orderId"));
            if(orderId<=0) throw new RuntimeException("ERROR OrderId:"+orderId);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

        if(orderService.deleteOrder(orderId)){
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS);
        }

        return  ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);




    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public Map create(@RequestBody Map<String, String> map, HttpServletRequest request){


//        for(Map.Entry<String,String> e:map.entrySet()){
//            System.out.println("key:"+e.getKey()+"|| value:"+e.getValue());
//        }
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);


        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)) return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);

            //参数校验
            String remark,orderNumber,goodsname;
            int status;
            Long endTime,needNum,supplierID;

            try {
                orderNumber =map.get("orderNumber");
                goodsname =map.get("goodsName");
                remark =map.get("remark");
                supplierID = Long.parseLong(map.get("type"));
                status = Integer.parseInt(map.get("status"));
                endTime = Long.parseLong(map.get("endTime"));
                needNum = Long.parseLong(map.get("needNum"));
                System.out.println("timestamp:"+endTime+"|| status"+status);

            }catch (Exception e){
                e.printStackTrace();
                return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderStat(0);
            orderInfo.setBuyerID(user.getUuid());
            orderInfo.setSupplierID(supplierID);
            orderInfo.setGoodsName(goodsname);
            orderInfo.setStartTime(System.currentTimeMillis());
            orderInfo.setEndTime(endTime);
            orderInfo.setBuyerName(user.getName());
            orderInfo.setNeedNum(needNum);
            orderInfo.setUndoNum(needNum);
            orderInfo.setOrderNumber(orderNumber);

            OrderInfo order = orderService.createOrder(orderInfo);
            if(order!=null){
                System.out.println("ssss");
                return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,order);
            }
            //return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,data);
        return null;
    }




    @RequestMapping(value = "/buyer/confirmDetail",method = RequestMethod.POST)
    @ResponseBody
    public Map bconfirm(@RequestBody Map<String, String> map, HttpServletRequest request){
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);

        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER))return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);


        long orderId=0,detailId=0;
        int type=0;
        try{
            detailId = Long.parseLong(map.get("detailId"));
            orderId = Long.parseLong(map.get("orderId"));
            type = Integer.parseInt(map.get("type"));

            if(type!=0&&type!=1)throw new RuntimeException("ERROR_TYPE");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

        OrderInfo orderInfo = orderService.getOrderInfo(orderId);

        if(orderInfo==null||orderInfo.getOrderStat()!=OrderState.DETAIL)
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ORDER_CHANGE);

        if(orderService.confirmDetail(detailId)){
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS);
        }

        //Detail orderDetail = orderService.getOrderDetail(orderId, detailId);


        return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);

    }

    @RequestMapping(value = "/supplier/confirm",method = RequestMethod.POST)
    @ResponseBody
    public Map sconfirm(@RequestBody Map<String, String> map, HttpServletRequest request){

        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);

        if(!Utils.checkAuth(user.getRoles(),RoleEnum.SUPPLIER))return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);

        long orderId=0;
        int type=0;
        try{
            orderId = Long.parseLong(map.get("orderId"));
            type = Integer.parseInt(map.get("type"));

            if(type!=0&&type!=1)throw new RuntimeException("ERROR_TYPE");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

        OrderInfo orderInfo = orderService.getOrderInfo(orderId);

        if(orderInfo==null||orderInfo.getSupplierID()!=user.getUuid())
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);


        //两种状态
        if((orderInfo.getOrderStat()!=OrderState.CONFIRM&&orderInfo.getOrderStat()!=OrderState.PUBLISH))
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ORDER_CHANGE);

        int state = type==0?OrderState.PUBLISH:OrderState.CONFIRM;

        if(orderService.setOrderStatus(orderId,state )){
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,state);
        }

        return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);
    }




    @RequestMapping(value = "/confirm/detail",method = RequestMethod.POST)
    @ResponseBody
    public Map confirmDetail(@RequestBody List<Map<String,String>> list, HttpServletRequest request) {

        for(Map<String,String> m:list){
            for(Map.Entry<String,String> e:m.entrySet()){
                System.out.println("key:"+e.getKey()+"|| value:"+e.getValue());
            }
        }

        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);


        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER))return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);

        long orderId,totalNum=0;
        List<Detail> dataList = new ArrayList<>();




        OrderInfo orderInfo = null;
        try{
            orderId = Long.parseLong(list.get(0).get("orderId"));


            if(orderId<=0)return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);

            orderInfo = orderService.getOrderInfo(orderId);

            if(orderInfo==null||orderInfo.getOrderStat()!=OrderState.CREATED)
                return ResponseUtil.genResponse(ERROR_CODE_TYPE.ORDER_CHANGE);

            if(orderInfo==null)return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);



            for(Map<String,String> m:list){
                Long time = Long.parseLong(m.get("time"));
                int actualNum = Integer.parseInt(m.get("actualNum"));
                totalNum+=actualNum;
                int state = Integer.parseInt(m.get("state"));
                int expectNum = Integer.parseInt(m.get("expectNum"));


                Detail detail = new Detail(expectNum, actualNum, time, orderId);
                detail.setState(state);

                String sid = m.get("id");
                if(sid!=null){
                    long id = Long.parseLong(sid);
                    detail.setId(id);
                }
                dataList.add(detail);
            }

            if(totalNum>orderInfo.getNeedNum())
                return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER,"超过订购数量");

            if(orderService.updateOrderDetails(orderId, dataList)){
                orderInfo.setUndoNum(orderInfo.getNeedNum()-totalNum);
                //TODO 接口待优化
                orderService.updateOrder(orderInfo) ;
                return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS);
            }

            return  ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

    }



    @Deprecated
    private void handleDetail(List<Detail> updateList) {
        if(updateList==null||updateList.size()==0)return;


       /* Set<Detail> unSet= new TreeSet<>(new Comparator<Detail>() {
            @Override
            public int compare(Detail o1, Detail o2) {
                return o1.getTime()>o2.getTime()?1:o1.getTime()==o2.getTime()?0:-1;
            }
        });*/

        List<Detail> unList = new LinkedList<>();

        Iterator<Detail> iterator = updateList.iterator();

        while(iterator.hasNext()){
            Detail next = iterator.next();
            if(next.getExpectNum()==0){
                unList.add(next);
            }else{
                iterator.remove();
            }
        }

        Collections.sort(unList, new Comparator<Detail>() {
            @Override
            public int compare(Detail o1, Detail o2) {
                return o1.getTime()>o2.getTime()?1:o1.getTime()==o2.getTime()?0:-1;
            }
        });


        //现在有两个集合 一个预期集合 一个实际集合


        int tempNum=0;
        for(Detail d:updateList){


            for(Detail act:unList){

                //1 找到每一个实际交货 时间在它之前的
                if(act.getTime()<=d.getTime()){
                    int dNum = d.getActualNum();
                    int expectNum = d.getExpectNum();
                    int actNum = act.getActualNum();



                }


            }




        }




    }

    @RequestMapping(value = "/getDetail/all",method = RequestMethod.POST)
    @ResponseBody
    public Map getallDetail(@RequestBody Map<String, String> map, HttpServletRequest request) {

        for(Map.Entry<String,String> e:map.entrySet()){
            System.out.println("key:"+e.getKey()+"|| value:"+e.getValue());
        }

        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);
        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER))return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);

        long orderId;
        try{
            orderId = Long.parseLong(map.get("orderId"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }


        List<Detail> allOrderDetail = orderService.getAllOrderDetail(orderId);

        System.out.println("执行完毕");
        return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,allOrderDetail);


    }




    @RequestMapping(value = "/confirm/send",method = RequestMethod.POST)
    @ResponseBody
    public Map confirmDetailSend(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String token = request.getHeader("Token");
        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        if(user==null&&user.getUuid()<=0)ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN);
        if(!Utils.checkAuth(user.getRoles(),RoleEnum.BUYER))return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);


        long orderId=0;
        try{
            orderId = Long.parseLong(map.get("orderId"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }


        OrderInfo orderInfo = orderService.getOrderInfo(orderId);

        if(orderInfo==null||orderInfo.getOrderStat()!=OrderState.DETAIL)
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ORDER_CHANGE);

         if(orderService.setOrderStatus(orderId, OrderState.CREATED))
             return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS);
         else
             return  ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);


    }
    @RequestMapping(value = "/create/detail",method = RequestMethod.POST)
    @ResponseBody
    public Map createDetail(@RequestBody Map<String, String> map, HttpServletRequest request) {
        for(Map.Entry<String,String> e:map.entrySet()){
            System.out.println("key:"+e.getKey()+"|| value:"+e.getValue());
        }

        long orderId,totalNum=0;
        Map<Long,Long> datamap = new HashMap<>();

        List<Detail> list = new ArrayList<>();


        try{
            //System.out.println("奇书："+ map.size()%2);
            if(map==null||map.size()==0||map.size()%2!=1)return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);

            orderId = Long.parseLong(map.get("orderId"));

            if(orderId<=0)return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);

            OrderInfo orderInfo = orderService.getOrderInfo(orderId);

            if(orderInfo==null)return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);

            Long endTime = orderInfo.getEndTime();
            long undoNum = orderInfo.getUndoNum();


            int size = (map.size()-1)/2;

            for(int i=0;i<size;i++){
                Long time = Long.parseLong(map.get("t" + i));
                if(time>endTime)return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER,"时间不能超过订单最终交货时间");
                int num = Integer.parseInt(map.get("n"+ i));
                totalNum+=num;

                list.add(new Detail(num,0,time,orderId));
            }
            if(totalNum!=undoNum)return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER,"请核对订单数量");

        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }


        if(orderService.createOrderDetail(orderId, list)) return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,list);



        return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);
    }
}
