package com.jhj.service;

import com.jhj.comm.PageModel;
import com.jhj.comm.TypeModel;
import com.jhj.pojo.user.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {

    //1 登陆
    public UserInfo login(String username, String password);

    //2 创建供应商
    Long createSupplier(long buyerId,String username,String password,String name,long phoneNumber);

    PageModel getSupplierByPage(Long uuid, int limit, int page);

    boolean deleteSupplier(Long uuid);

    TypeModel getSupplierType(Long uuid);
}
