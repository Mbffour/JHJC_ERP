package com.jhj.cash.RemovalListener;

import com.jhj.pojo.user.UserInfo;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.log4j.Logger;

public class AuthCacheRemovalListener implements RemovalListener<String, UserInfo> {

	private static final Logger logger = Logger.getLogger("business");















  @Override
  public void onRemoval(RemovalNotification<String, UserInfo> notification) {
	  //获取当前移除的token
	  String token = notification.getKey();
	  long uuid = notification.getValue().getUuid();
	  RemovalCause removeCause = notification.getCause();
	  System.out.println("Remove Token Cache | " + uuid + " | " + removeCause.toString());
	  logger.warn("Remove Token Cache | " + uuid + " | " + removeCause.toString());
//	  TokenContainer.getInstance().removeByToken(notification.getValue().getUserInfo().getUuid(),token);
//      logger.warn("Remove Auth Cache | " + uuid +" TOKENCONTAINER_MAP_SIZE  |  " + TokenContainer.getInstance().getOnLineUserCount() +"   |   AUTH_CACHE_SIZE  " + CacheManager.getInstance().AUTH_CACHE.size() +  "   |   LEFT_UUID_MAP_SIZE  " + (TokenContainer.getInstance().map.get(uuid) == null ? null : TokenContainer.getInstance().map.get(uuid).mTokenMap.size())+ " | " + removeCause.toString());
      
  }
}
