package com.redis.transaction;

import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.entity.TestMutexEntity;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.factory.TEntityFactory;
import com.redis.transaction.force.ForceEntity;
import com.redis.transaction.timelock.TestTimeMutexEntity;
import com.redis.transaction.wait.WaitMutexEntity;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class TEntityFactoryImpl extends TEntityFactory {

    public  static TestMutexEntity createTestMutexEntity(String name, RedisDaoImpl redisService, String redisKey, String union){
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(name, redisKey, union);
        TestMutexEntity testMutexEntity = new TestMutexEntity(name, union, redisService);
        return testMutexEntity;
    }

    public  static ForceEntity createForceEntity(String cause, RedisDaoImpl redisService, String redisKey, String union, int seconds){
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(cause, redisKey, union);
        ForceEntity forceEntity = new ForceEntity(cause, union, redisService, TLockType.FORCE_WRITE_TIME, seconds);
        return forceEntity;
    }

    public  static TestTimeMutexEntity createTestTimeMutexEntity(String cause, RedisDaoImpl redisService, String redisKey, String union){
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(cause, redisKey, union);
        TestTimeMutexEntity testTimeMutexEntity = new TestTimeMutexEntity(cause, union, redisService);
        return testTimeMutexEntity;
    }

    public  static WaitMutexEntity createWaitMutexEntity(String cause, RedisDaoImpl redisService, String redisKey, String union){
        String key = GameTransactionKeyFactoryImpl.getPlayerTransactionEntityKey(cause, redisKey, union);
        WaitMutexEntity waitTimeMutexEntity = new WaitMutexEntity(cause, union, redisService);
        return waitTimeMutexEntity;
    }
}
