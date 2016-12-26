package com.redis.transaction.db;

import com.redis.config.GlobalConstants;
import com.redis.util.StringUtils;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class RedisKeyUtil {

    /**
     * @param name
     * @param redisKey
     * @param uuid
     * @return
     */
    public static String getCommonEntityKey(String redisKey, String name, String uuid) {
        return redisKey + name + GlobalConstants.Strings.commonSplitString + uuid;
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


}
