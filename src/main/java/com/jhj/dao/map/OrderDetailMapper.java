package com.jhj.dao.map;

import com.jhj.pojo.order.Detail;
import com.jhj.pojo.order.OrderInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailMapper implements RowMapper<Detail> {
    @Override
    public Detail mapRow(ResultSet rs, int i) throws SQLException {

        long order_id = rs.getLong("order_id");
        int expectNum = rs.getInt("expectNum");

        int actualNum = rs.getInt("actualNum");
        long expect_time = rs.getLong("expect_time");
        int state = rs.getInt("state");
        long id = rs.getLong("id");

        Detail detail = new Detail(expectNum, actualNum, expect_time, order_id);
        detail.setState(state);
        detail.setId(id);
        return detail;
    }
}
