package com.jhj.service.impl;


import com.jhj.cash.CacheManager;
import com.jhj.comm.PageModel;
import com.jhj.comm.RoleEnum;
import com.jhj.comm.TypeModel;
import com.jhj.dao.UserDao;
import com.jhj.pojo.user.Supplier;
import com.jhj.pojo.user.UserInfo;
import com.jhj.service.UserService;
import com.jhj.token.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService ,Serializable {
    @Autowired
    UserDao userDao;


    @Override
    public UserInfo login(String username, String password) {

        UserInfo userByLogin = userDao.getUserByLogin(username, password);

        System.out.println("service :"+userByLogin);

        if(userByLogin==null||userByLogin.getUuid()<=0)
            return null;

        String token = RSAUtil.initKey();
        System.out.println("token"+token);

        //角色和token
        if(userDao.setUserToken(userByLogin.getUuid(), token)>0){
            List<String> rolelist = userDao.getUserRoleName(userByLogin.getUuid());
            if(rolelist!=null&&rolelist.size()>0){
                userByLogin.setToken(token);
                userByLogin.setRoles(userDao.getUserRoleName(userByLogin.getUuid()));
                CacheManager.getInstance().AUTH_CACHE.put(userByLogin.getToken(),userByLogin);
                return userByLogin;
            }
        }

        return null;
    }

    @Override
    public Long createSupplier(long buyerId, String username, String password, String name, long phoneNumber) {

        /*
          考虑事务优化 保证原子性
         */
        //1  创建  返回的是主健id
        long userid = userDao.createUser(username, password, name, phoneNumber);
        if(userid<=0)
            return null;

        //2 分配角色
        int i = userDao.dispatherRole(userid, RoleEnum.SUPPLIER.intValue());

        //3 链接关系
        int relate = userDao.relateUser(buyerId, userid);

        if(relate<=0){
            userDao.deleteUser(userid);
            userDao.dispatherRole(userid, RoleEnum.NULL.intValue());
            return null;
        }
        return userid;
    }

    @Override
    public PageModel<UserInfo> getSupplierByPage(Long uuid, int limit, int page) {
        //计算
        int start = limit*(page-1);

        //1 查所有userid

        List<Long> allIds = userDao.getAllSupplierID(uuid);

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

           return PageModel.genData(userDao.getUserByIds(targerIds),allIds.size());
        }

        return  null;
    }

    @Override
    public boolean deleteSupplier(Long uuid) {
        //删除供应商

        return userDao.deleteUser(uuid)>0?true:false;
    }

    @Override
    public  TypeModel getSupplierType(Long uuid) {

        List<Long> allIds = userDao.getAllSupplierID(uuid);

        if(allIds==null&&allIds.size()==0)
            return null;

        List<UserInfo> userByIds = userDao.getUserByIds(allIds);


        if(userByIds==null||userByIds.size()==0)
            return null;



        TypeModel data = new TypeModel<String, Long, UserInfo>(userByIds) {
            @Override
            public Long fillValue(UserInfo userInfo) {
                System.out.println("getValue "+userInfo.getUuid());
                return userInfo.getUuid();
            }

            @Override
            public String fillKey(UserInfo userInfo) {
                return userInfo.getName();
            }
        };




        return data;
    }
}
