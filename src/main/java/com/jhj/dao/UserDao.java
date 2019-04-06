package com.jhj.dao;

import com.jhj.pojo.user.Supplier;
import com.jhj.pojo.user.UserInfo;

import java.util.List;

public interface UserDao {

    public UserInfo getUserByLogin(String username, String password);

    public int setUserToken(long uuid,String token);


    public List<String> getUserRoleName(long uuid);

    long createUser(String username, String password, String name, long phoneNumber);


    int  relateUser(long buyerID,long supplierID);

    int  deleteUser(long userid);


    List<Long> getAllSupplierID(Long uuid);

    List<UserInfo> getUserByIds(List<Long> ids);


    UserInfo getUser(Long uuid);

    int dispatherRole(long userid, int roleType);
}
