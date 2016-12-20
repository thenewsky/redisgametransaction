package com.redis.transaction.db;

import com.redis.config.GlobalConstants;
import com.redis.util.StringUtils;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class RedisKeyUtil {

    /**
     * @param cause
     * @param redisKey
     * @param union
     * @return
     */
    public static String getCommonTransactionEntityKey(String cause, String redisKey, String union) {
        return redisKey + cause + GlobalConstants.Strings.commonSplitString + union;
    }


    /**
     * 获取锁Key
     *
     * @param lockKey
     * @param tEntityName
     * @return
     */
    public static String getLockKey(String lockKey, String tEntityName) {
        return lockKey + "#" + tEntityName;
    }

    /**
     * 获取信息
     *
     * @return
     */
//    public String getInfo() {
//        return lockKey + StringUtils.DEFAULT_SPLIT + tEntityName.toString() + StringUtils.DEFAULT_SPLIT + this.lockState;
//    }
}
