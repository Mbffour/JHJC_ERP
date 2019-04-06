package com.jhj.interceptors;

import com.google.gson.Gson;
import com.jhj.comm.ERROR_CODE_TYPE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

  public static void doErrorResponse(HttpServletRequest request, HttpServletResponse response, ERROR_CODE_TYPE iErrCode) throws IOException {
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("code", iErrCode.intValue());
    PrintWriter out = response.getWriter();
    out.write(new Gson().toJson(result));
    out.close();
  }

  public static Map genResponse(ERROR_CODE_TYPE code,Object data){

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("code", code.intValue());
    result.put("data",data);
    return result;
  }

  public static Map genResponse(ERROR_CODE_TYPE code){

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("code", code.intValue());
    result.put("data",code.info());
    return result;
  }




}
