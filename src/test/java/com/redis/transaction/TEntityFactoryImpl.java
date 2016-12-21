package com.redis.transaction;

import com.redis.transaction.db.RedisDao;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.job.entity.TestMutexEntity;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.factory.TJobEntityFactory;
import com.redis.transaction.force.ForceEntity;
import com.redis.transaction.timelock.TestTimeMutexEntity;
import com.redis.transaction.wait.WaitMutexEntity;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class TEntityFactoryImpl extends TJobEntityFactory {

    public static TestMutexEntity createTestMutexEntity(String name, RedisDao redisDao, String redisKey, String union) {
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(name, redisKey, union);
        TestMutexEntity testMutexEntity = new TestMutexEntity(redisDao, name, union);
        return testMutexEntity;
    }

    public static ForceEntity createForceEntity(RedisDaoImpl redisDao, String cause, String redisKey, String union, int seconds) {
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(cause, redisKey, union);
        ForceEntity forceEntity = new ForceEntity(redisDao, cause, union, TLockType.FORCE_WRITE_TIME, seconds);
        return forceEntity;
    }

    public static TestTimeMutexEntity createTestTimeMutexEntity(RedisDaoImpl redisDao, String key_pre, String entity_name, String union) {
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(key_pre, entity_name, union);
        TestTimeMutexEntity testTimeMutexEntity = new TestTimeMutexEntity(redisDao, key, union);
        return testTimeMutexEntity;
    }

    public static WaitMutexEntity createWaitMutexEntity(RedisDaoImpl redisDao, String cause, String redisKey, String union) {
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(redisKey, cause, union);
        WaitMutexEntity waitTimeMutexEntity = new WaitMutexEntity(redisDao, cause, union);
        return waitTimeMutexEntity;
    }
}
