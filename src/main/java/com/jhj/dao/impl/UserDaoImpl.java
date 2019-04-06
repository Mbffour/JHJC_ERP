package com.jhj.dao.impl;

import com.jhj.dao.UserDao;
import com.jhj.dao.map.LongMapper;
import com.jhj.dao.map.RoleMapper;
import com.jhj.dao.map.UserMapper;
import com.jhj.pojo.user.Supplier;
import com.jhj.pojo.user.UserInfo;
import com.jhj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public UserInfo getUserByLogin(String username, String password){
        String sql = "select * from t_user_info where username=? and password=?";

        UserInfo userInfo =null;
        try{
            userInfo = jdbcTemplate.queryForObject(sql, new Object[]{username, password}, new UserMapper());
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return userInfo;
    }

    @Override
    public int setUserToken(long uuid, String token) {
        //dbcTemplate.update("UPDATE  account SET NAME=? ,money=? WHERE id=?",
        String sql = "UPDATE t_user_info SET token = ? WHERE  uuid=?";
        return jdbcTemplate.update(sql,token,uuid);
    }

    @Override
    public List<String> getUserRoleName(long uuid) {
        String sql = "Select roleid from t_user_role where uuid = ?";

        List<String> rolesName = jdbcTemplate.query(sql, new Object[]{uuid}, new RoleMapper());
        return rolesName;
    }

    @Override
    public long createUser(final String username, final String password, final String name, final long phoneNumber) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                String sql = "insert into t_user_info(username,password,nickname,phonenum) values(?,?,?,?) ";

                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,username);
                ps.setString(2,password);
                ps.setString(3,name);
                ps.setLong(4,phoneNumber);
                return ps;
            }
        }, keyHolder);

        if(update<=0)
            return update;

        return keyHolder.getKey().longValue();
    }

    @Override
    public int relateUser(long buyerID, long supplierID) {
        String sql = "insert into t_user_relate(buyer_id,supplier_id) values(?,?)";
        return jdbcTemplate.update(sql,buyerID,supplierID);
    }

    @Override
    public int deleteUser(long userid) {
        String sql = "delete from t_user_info where uuid=?";
        String sql2 = "delete from t_user_role where uuid=?";
        String sql3 = "delete from t_user_relate where buyer_id=?";
        String sql4 = "delete from t_user_relate where supplier_id=?";

        jdbcTemplate.update(sql,userid);
        jdbcTemplate.update(sql2,userid);
        jdbcTemplate.update(sql3,userid);
        jdbcTemplate.update(sql4,userid);

        return 1;
    }

    @Override
    public List<Long> getAllSupplierID(Long uuid) {
        String sql = "select supplier_id from t_user_relate where buyer_id = ?";
        List<Long> query = jdbcTemplate.query(sql, new Object[]{uuid}, new LongMapper("supplier_id"));
        return query;
    }

    @Override
    public List<UserInfo> getUserByIds(List<Long> ids) {

        if(ids==null||ids.size()==0)
            return null;

        String sql = "select * FROM t_user_info where uuid in( ";

        for(Long uuid:ids){
            sql = sql+uuid+",";
        }
        sql = sql.substring(0,sql.length()-1)+" )";

        System.out.println(sql);

        List<UserInfo> query = jdbcTemplate.query(sql, new UserMapper());

        return query;
    }

    @Override
    public UserInfo getUser(Long uuid) {

        String sql = "select * FROM t_user_info where uuid=?";

        UserInfo userInfo =null;
        try{
            userInfo = jdbcTemplate.queryForObject(sql, new Object[]{uuid}, new UserMapper());
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return userInfo;
    }

    @Override
    public int dispatherRole(long userid, int roleType) {
       String sql = "insert into t_user_role(uuid,roleid) values(?,?)";
        return jdbcTemplate.update(sql,userid,roleType);
    }


}
