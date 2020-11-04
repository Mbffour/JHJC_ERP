package com.jhj.cash;

import com.jhj.cash.RemovalListener.AuthCacheRemovalListener;
import com.jhj.pojo.user.UserInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheManager {
    private static  CacheManager instance;


    private int a ;
    private CacheManager() {}

    public static CacheManager getInstance() {
        if(instance == null) {
            synchronized (CacheManager.class) {
                instance = new CacheManager();
                sdasadsad

                        sadsadsad

                        sadsadsad
            }
        }
        return instance;
    }

    // 用户信息
    public Cache<String, UserInfo> AUTH_CACHE = CacheBuilder.newBuilder().maximumSize(8000l).expireAfterAccess(1800l, TimeUnit.SECONDS)
            .expireAfterWrite(1, TimeUnit.DAYS).removalListener(new AuthCacheRemovalListener()).recordStats().build();
}
