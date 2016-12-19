package com.redis.transaction.db;

import com.redis.config.GlobalConstants;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class RedisKeyUtil {

    /**
     *
     * @param cause
     * @param redisKey
     * @param union
     * @return
     */
    public static String getCommonTransactionEntityKey(String cause, String redisKey, String union) {
        return redisKey + cause + GlobalConstants.Strings.commonSplitString + union;
    }
}
