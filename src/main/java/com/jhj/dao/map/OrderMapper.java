package com.jhj.dao.map;

import com.jhj.pojo.order.OrderInfo;
import com.jhj.pojo.user.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<OrderInfo> {
    @Override
    public OrderInfo mapRow(ResultSet rs, int i) throws SQLException {
        try{
            OrderInfo orderInfo = new OrderInfo();

            orderInfo.setOrderId(rs.getLong("order_id"));
            orderInfo.setOrderNumber(rs.getString("order_number"));
            orderInfo.setGoodsName(rs.getString("goods_name"));
            orderInfo.setStartTime(rs.getLong("start_time"));
            orderInfo.setEndTime(rs.getLong("end_time"));
            orderInfo.setNeedNum(rs.getLong("need_num"));
            orderInfo.setUndoNum(rs.getLong("undo_num"));
            orderInfo.setSupplierID(rs.getLong("supplier_id"));
            orderInfo.setBuyerID(rs.getLong("buyer_id"));
            orderInfo.setOrderStat(rs.getInt("order_status"));
            System.out.println("mapRow ->"+orderInfo);
            return orderInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
