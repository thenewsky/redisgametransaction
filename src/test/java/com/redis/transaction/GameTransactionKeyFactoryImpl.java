package com.redis.transaction;

import com.redis.transaction.db.RedisKeyUtil;
import com.redis.config.GlobalConstants;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class GameTransactionKeyFactoryImpl extends RedisKeyUtil {
    /**
     * 获取玩家锁
     *
     * @param cause
     * @return
     */
    public static String getPlayerTransactionEntityKey(String cause, String redisKey, String union) {
        return redisKey + cause + GlobalConstants.Strings.commonSplitString + union;
    }


}
