package com.redis.transaction.wait;

import com.redis.transaction.db.RedisDao;
import com.redis.transaction.job.entity.AbstractTJobEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;

/**
 * Created by jiangwenping on 16/12/15.
 */
public class WaitMutexEntity extends AbstractTJobEntity {

    private RedisDao redisDao;

    public WaitMutexEntity(RedisDao redisDao, String cause, String key) {
        super(redisDao, cause, key);
        this.redisDao = redisDao;

    }

    @Override
    public void commit() throws TException {
        String testRedisKey = "testRedis";
        redisDao.setString(testRedisKey, "1000");
    }

    @Override
    public void rollback() throws TException {

    }

    @Override
    public CommitResult trycommit() throws TException {
//        String testRedisKey =  "testRedis";
//        if(redisDao.getString(testRedisKey).equals("1000")){
//            return CommitResult.COMMON_ERROR;
//        }
        return CommitResult.SUCCESS;
    }


}
