package com.redis.transaction.timelock;

import com.redis.transaction.entity.AbstractTEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.exception.TException;
import com.redis.transaction.service.RedisService;
import com.redis.util.TimeUtil;

/**
 * Created by jiangwenping on 16/12/9.
 */
public class TestTimeMutexEntity extends AbstractTEntity {

    private RedisService redisService;
    public TestTimeMutexEntity(String cause, String key, RedisService redisService) {
        super(cause, key, redisService, TLockType.WRITE_TIME, TimeUtil.FIVE_MINUTE);
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

