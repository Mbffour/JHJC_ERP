package com.jhj.dao.impl;

import com.jhj.dao.OrderDao;
import com.jhj.dao.map.LongMapper;
import com.jhj.dao.map.OrderDetailMapper;
import com.jhj.dao.map.OrderMapper;
import com.jhj.dao.map.UserMapper;
import com.jhj.pojo.order.Detail;
import com.jhj.pojo.order.OrderInfo;
import com.jhj.pojo.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Long createOrder(final OrderInfo orderInfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                String sql = "insert into t_order_info(order_number,goods_name,start_time,end_time,need_num,undo_num,supplier_id,buyer_id,order_status) values(?,?,?,?,?,?,?,?,?) ";

                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,orderInfo.getOrderNumber());
                ps.setString(2,orderInfo.getGoodsName());
                ps.setLong(3,orderInfo.getStartTime());
                ps.setLong(4,orderInfo.getEndTime());
                ps.setLong(5,orderInfo.getNeedNum());
                ps.setLong(6,orderInfo.getUndoNum());
                ps.setLong(7,orderInfo.getSupplierID());
                ps.setLong(8,orderInfo.getBuyerID());
                ps.setLong(9,orderInfo.getOrderStat());
                return ps;
            }
        }, keyHolder);

        if(update<=0)
            return (long)update;

        return keyHolder.getKey().longValue();
    }


    @Override
    public List<Long> getAllOrderID(Long uuid,boolean bBuyer,Long supplierId) {
        String sql = "select order_id from t_order_info ";
        Object[] objects;

        if(bBuyer){
            sql +="where buyer_id=?";
        }else{
            sql +="where supplier_id=? and order_status>0";
        }

        if(supplierId!=null){
            sql += " and supplier_id=?";
            objects=new Object[]{uuid,supplierId};
        }else{
            objects=new Object[]{uuid};
        }

        List<Long> query = jdbcTemplate.query(sql, objects, new LongMapper("order_id"));
        return query;
    }

    @Override
    public List<OrderInfo> getOrderByIds(List<Long> ids) {
        if(ids==null||ids.size()==0)
            return null;

        String sql = "select * FROM t_order_info where order_id in( ";

        for(Long orderid:ids){
            sql = sql+orderid+",";
        }
        sql = sql.substring(0,sql.length()-1)+" )";

        System.out.println(sql);

        List<OrderInfo> query = jdbcTemplate.query(sql, new OrderMapper());

        return query;
    }

    @Override
    public int updateOrderStatus(long orderId, int status) {
        //dbcTemplate.update("UPDATE  account SET NAME=? ,money=? WHERE id=?",
        String sql = "UPDATE t_order_info SET order_status = ? WHERE  order_id=?";
        return jdbcTemplate.update(sql,status,orderId);
    }

    @Override
    public OrderInfo getOrder(Long orderId) {
        String sql = "select * FROM t_order_info where order_id =? ";
        OrderInfo orderInfo = null;
        try{
            orderInfo = jdbcTemplate.queryForObject(sql, new Object[]{orderId}, new OrderMapper());
        }catch (EmptyResultDataAccessException e){
             //ignore
        }
        return orderInfo;
    }

    @Override
    public int updateOrder(OrderInfo orderInfo) {
        //order_number,goods_name,start_time,end_time,need_num,undo_num,supplier_id,buyer_id,order_status
        String sql = "update t_order_info set order_number=?,goods_name=?,start_time=?,end_time=?,need_num=?,undo_num=?,supplier_id=?,buyer_id=?,order_status=? where order_id =? ";


        return jdbcTemplate.update(sql,
                orderInfo.getOrderNumber(),
                orderInfo.getGoodsName(),
                orderInfo.getStartTime(),
                orderInfo.getEndTime(),
                orderInfo.getNeedNum(),
                orderInfo.getUndoNum(),
                orderInfo.getSupplierID(),
                orderInfo.getBuyerID(),
                orderInfo.getOrderStat(),
                orderInfo.getOrderId()
                );
    }

    @Override
    public int deleteOrder(Long orderId) {
        String sql ="delete from t_order_info where order_id=?";
        return jdbcTemplate.update(sql,orderId);
    }

    @Override
    public List<Detail> getOrderDetails(Long orderId, int limit, int page) {
        String sql = "select * from t_order_detail where order_id = ? limit ?,?";
        return jdbcTemplate.query(sql,new Object[]{orderId,limit,page},new OrderDetailMapper());
    }

    @Override
    public List<Detail> getAllOrderDetails(Long orderId) {
        String sql = "select * from t_order_detail where order_id = ? ";
        return jdbcTemplate.query(sql,new Object[]{orderId},new OrderDetailMapper());
    }

    @Override
    public boolean createOrderDetails(long orderId, final List<Detail> list) {

        String sql = "insert into t_order_detail(order_id,expectNum,actualNum,expect_time,state) values(?,?,?,?,?) on duplicate key update id=?,order_id=?,expectNum=?,actualNum=?,expect_time=?";

        try{
            int[] ints = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, list.get(i).getOrderId());
                    ps.setLong(2, list.get(i).getExpectNum());
                    ps.setLong(3, list.get(i).getActualNum());
                    ps.setLong(4, list.get(i).getTime());
                    ps.setLong(5, list.get(i).getState());

                    ps.setLong(6, list.get(i).getId());
                    ps.setLong(7, list.get(i).getOrderId());
                    ps.setLong(8, list.get(i).getExpectNum());
                    ps.setLong(9, list.get(i).getActualNum());
                    ps.setLong(10, list.get(i).getTime());
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Long getOrderDetailCount(Long orderId) {
        String sql = "select count(*)as result from t_order_detail where order_id=?";
        long count=0;
        try{
            count = jdbcTemplate.queryForObject(sql, new Object[]{orderId}, new LongMapper("result"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Detail getOrderDetail(long detailId) {
        String sql = "select * from t_order_detail where id=? ";
        Detail detail = null;
        try{
            detail =  jdbcTemplate.queryForObject(sql, new Object[]{detailId}, new OrderDetailMapper());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return detail;
    }

    @Override
    public void setOrderDetailState(long detailId) {
        String sql = "update t_order_detail set state = 1  where id = ?";
        jdbcTemplate.update(sql,detailId);
    }

    @Override
    public boolean deleteAllOrderDetail(long orderId) {
        String sql = "delete from t_order_detail  where order_id = ? and state=1";
        jdbcTemplate.update(sql,orderId);
        return true;
    }

    @Override
    public void deleteOrderBySupplier(long supplierid,long buyid) {

        String sql1 = "delete from t_order_detail where order_id in (select order_id from t_order_info where supplier_id = ? and buyer_id=?)";
        String sql2 = "delete from t_order_info where supplier_id = ? and buyer_id=?";


        jdbcTemplate.update(sql1,supplierid,buyid);
        jdbcTemplate.update(sql2,supplierid,buyid);
    }
}
