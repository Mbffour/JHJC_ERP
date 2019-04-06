package com.jhj.interceptors;

import com.jhj.cash.CacheManager;
import com.jhj.comm.ERROR_CODE_TYPE;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class AuthInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {



        String token = request.getHeader("Token");
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("AuthInterceptors url"+requestURL);
        System.out.println("AuthInterceptors token"+token);
        if (StringUtils.isBlank(token)||CacheManager.getInstance().AUTH_CACHE.getIfPresent(token)==null) {
            ResponseUtil.doErrorResponse(request, response, ERROR_CODE_TYPE.ILLEGAL_TOKEN);
            System.out.println("AuthInterceptors token ERROR "+requestURL);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
