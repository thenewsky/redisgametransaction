package com.redis.transaction.factory;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.db.RedisKeyUtil;
import com.redis.transaction.job.entity.TJobEnityImpl;
import org.slf4j.Logger;

public class TJobEntityFactory {

    protected static Logger transactionLogger = Loggers.transactionLogger;

    /**
     * 获取通用读锁实体 默认不能读取到
     *
     * @return
     */
    public static TJobEnityImpl createReadRejectTEnity(String cause, RedisDaoImpl redisService, String redisKey, String unionKey) {
        TJobEnityImpl commonReadTransactionEnity = createReadTEnity(cause, redisService, redisKey, unionKey);
        commonReadTransactionEnity.setRejectFlag(true);
        return commonReadTransactionEnity;
    }

    /**
     * 获取通用读锁实体 默认需要读取到
     *
     * @return
     */
    public static TJobEnityImpl createReadTEnity(String name, RedisDaoImpl redisService, String redisKey, String unionKey) {
        transactionLogger.debug("<debug-add-entity>: " + "TJobEntityFactory:        new TJobEnityImpl(name, RedisKeyUtil.getCommonTransactionEntityKey(name, redisKey, unionKey), redisService)");
        return new TJobEnityImpl(name, RedisKeyUtil.getCommonTransactionEntityKey(name, redisKey, unionKey), redisService);


    }
}
