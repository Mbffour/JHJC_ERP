package com.jhj.dao.map;

import com.jhj.comm.Utils;
import com.jhj.pojo.user.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet rs, int i) throws SQLException {
        if(rs==null)
            return null;

        try{
            int roleid = rs.getInt("roleid");
            return Utils.transformRole(roleid);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
