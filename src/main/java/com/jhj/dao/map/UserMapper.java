package com.jhj.dao.map;

import com.jhj.pojo.user.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int i) throws SQLException {

        try{
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername( rs.getString("username"));
            userInfo.setName(rs.getString("nickname"));
            userInfo.setPhonenum(rs.getLong("phonenum"));
            userInfo.setPassword(rs.getString("password"));
            userInfo.setUuid(rs.getLong("uuid"));

            System.out.println("mapRow ->"+userInfo);
            return userInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
