package com.redis.transaction.wait;

import com.redis.transaction.entity.AbstractTEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import com.redis.transaction.service.RedisService;

/**
 * Created by jiangwenping on 16/12/15.
 */
public class WaitMutexEntity extends AbstractTEntity {

    private RedisService redisService;
    public WaitMutexEntity(String cause, String key, RedisService redisService) {
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
