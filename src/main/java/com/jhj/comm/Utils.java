package com.jhj.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    final static Map<Integer,String> roleMap= new HashMap<>();


    static {
    roleMap.put(1,"buyer");
    roleMap.put(2,"supplier");
    }
    public static String transformRole(int roleid){
        return roleMap.get(roleid);
    }

    public static boolean checkAuth(List<String> roles, RoleEnum roleEnum){

        if(roles!=null&&roles.size()>0&&roleEnum!=null){
            return roles.contains(roleEnum.strValue());
        }

        return false;
    }

}
