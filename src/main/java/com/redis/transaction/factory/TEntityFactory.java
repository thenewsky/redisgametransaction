package com.redis.transaction.factory;

import com.redis.transaction.db.RedisKeyUtil;
import com.redis.transaction.entity.TEnityImpl;
import com.redis.transaction.service.RedisService;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class TEntityFactory {
    /**
     * 获取通用读锁实体 默认不能读取到
     *
     * @return
     */
    public static TEnityImpl createReadRejectTEnity(String cause, RedisService redisService, String redisKey, String unionKey) {
        TEnityImpl commonReadTransactionEnity = createReadTEnity(cause, redisService, redisKey, unionKey);
        commonReadTransactionEnity.setRejectFlag(true);
        return commonReadTransactionEnity;
    }

    /**
     * 获取通用读锁实体 默认需要读取到
     *
     * @return
     */
    public static TEnityImpl createReadTEnity(String name, RedisService redisService, String redisKey, String unionKey) {
        return new TEnityImpl(name, RedisKeyUtil.getCommonTransactionEntityKey(name, redisKey, unionKey), redisService);
    }
}
