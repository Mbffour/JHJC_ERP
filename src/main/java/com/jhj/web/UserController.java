package com.jhj.web;

import com.jhj.cash.CacheManager;
import com.jhj.comm.*;
import com.jhj.pojo.user.Supplier;
import com.jhj.pojo.user.UserInfo;
import com.jhj.interceptors.ResponseUtil;
import com.jhj.service.UserService;
import com.jhj.token.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = {"http://localhost:9529", "null"})
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody Map<String, Object> map,HttpServletRequest request){


        String username = (String)map.get("username");
        String password = (String)map.get("password");



        UserInfo userInfo = userService.login(username, password);

        if(userInfo==null)
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ERROR_USERNAME_OR_PASSWORD,"用户名密码错误");


        System.out.println("请求完毕");
        return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,userInfo.getToken());
    }



    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ResponseBody
    public Map info(HttpServletRequest request){

        String token = request.getHeader("Token");

        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);

        System.out.println("user: "+user);
        if(user!=null){
            return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,user);
        }

        return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_TOKEN,null);
    }




    @RequestMapping(value = "/get/supplier",method = RequestMethod.POST)
    @ResponseBody
    public Map getAll(@RequestBody Map<String, Object> map,HttpServletRequest request){

        String token = request.getHeader("Token");

        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);

        int page,limit;
        try{
            page = (int)map.get("page");
            limit = (int)map.get("limit");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }
        //{"page":1,"limit":1,"sort":"+id"}

        if(Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)){
             PageModel data = userService.getSupplierByPage(user.getUuid(),limit,page);
             return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,data);
        }else{
            return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);

        }


    }


    @RequestMapping(value = "/get/supplierType",method = RequestMethod.POST)
    @ResponseBody
    public Map getAllSupplierType(HttpServletRequest request){

        String token = request.getHeader("Token");

        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);


        if(Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)){
            TypeModel data = userService.getSupplierType(user.getUuid());

            return  ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,data);
        }

        return  ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_AUTH);
    }



    @RequestMapping(value = "/delete/supplier",method = RequestMethod.POST)
    @ResponseBody
    public Map deleteSupplier(@RequestBody Map<String, String> map,HttpServletRequest request){
        String token = request.getHeader("Token");

        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);

        Long uuid = null;
        try {
            uuid = Long.parseLong(map.get("uuid"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }

        if(uuid!=null&&uuid>0&&Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)){


            if(userService.deleteSupplier(uuid)){
               return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,uuid);
            }

        }
        return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);
    }
    @RequestMapping(value = "/create/supplier",method = RequestMethod.POST)
    @ResponseBody
    public Map createSupplier(@RequestBody Map<String, Object> map,HttpServletRequest request){

        String token = request.getHeader("Token");

        UserInfo user = CacheManager.getInstance().AUTH_CACHE.getIfPresent(token);
        String supplierName,userName,passWord;
        Long phoneNumber;


        try{
            supplierName = (String)map.get("supplierName");
            System.out.println(supplierName);
            userName =  (String)map.get("userName");
            passWord =  (String)map.get("passWord");
            phoneNumber = Long.parseLong((String)map.get("phoneNumber"));
        }catch (Exception e){
            return ResponseUtil.genResponse(ERROR_CODE_TYPE.ILLEGAL_PARAMETER);
        }


        if(Utils.checkAuth(user.getRoles(),RoleEnum.BUYER)){

            Long uuid = userService.createSupplier(user.getUuid(), userName, passWord, supplierName, phoneNumber);
            if(uuid!=null&&uuid>0){

               //回包携带数据
               UserInfo userInfo = new UserInfo();
                userInfo.setUuid(uuid);
                userInfo.setName(supplierName);
                userInfo.setPassword(passWord);
                userInfo.setPhonenum(phoneNumber);
                userInfo.setUsername(userName);
               return ResponseUtil.genResponse(ERROR_CODE_TYPE.SUCCESS,userInfo);
           }
        }

        return ResponseUtil.genResponse(ERROR_CODE_TYPE.UNKNOWN);
    }
}
