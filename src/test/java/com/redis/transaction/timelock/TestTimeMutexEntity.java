package com.redis.transaction.timelock;

import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.job.entity.AbstractTJobEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.exception.TException;
import com.redis.util.TimeUtil;

/**
 * Created by jiangwenping on 16/12/9.
 */
public class TestTimeMutexEntity extends AbstractTJobEntity {

    private RedisDaoImpl redisService;

    public TestTimeMutexEntity(RedisDaoImpl redisService, String key_pre, String entity_name) {
        super(redisService, key_pre, entity_name, TLockType.WRITE_TIME, TimeUtil.FIVE_MINUTE);
        this.redisService = redisService;

    }

    @Override
    public void commit() throws TException {
        String testRedisKey = "testRedis";
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

    @Override
    public String getInfo() {
        return null;
    }

}

