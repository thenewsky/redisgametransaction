package com.redis.transaction.entity;

import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import com.redis.transaction.db.RedisDaoImpl;

/**
 * Created by jiangwenping on 16/12/6.
 * 设置一个数据到redis
 */
public class TestMutexEntity extends AbstractTEntity {

    private RedisDaoImpl redisService;
    public TestMutexEntity(String cause, String key, RedisDaoImpl redisService) {
        super(cause, key, redisService);
        this.redisService = redisService;

    }

    @Override
    public void commit() throws TException {
        String testRedisKey =  "testRedis";
        redisService.setString(testRedisKey, "1000");
    }

    @Override
    public void rollback() throws TException {

    }

    @Override
    public CommitResult trycommit() throws TException {
//        String testRedisKey =  "testRedis";
//        if(redisService.getString(testRedisKey).equals("1000")){
//            return CommitResult.COMMON_ERROR;
//        }
        return CommitResult.SUCCESS;
    }

}
