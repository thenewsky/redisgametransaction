package com.redis.transaction.job.entity;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.db.DBConfigManager;
import com.redis.transaction.job.TransactionJob;
import com.redis.transaction.job.TransactionJobImpl;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class TestMutexTransaction {
    public static void main(String[] args) throws Exception {

        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(DBConfigManager.getJedisPool());

        TransactionJob transactionService = new TransactionJobImpl();
        String union = "union";
        TestMutexEntity testMutexEntity = TEntityFactoryImpl.createTestMutexEntity(TEntityName.EDIT_A, redisService, RedisKey.player,  union);
        CommitResult commitResult = transactionService.commit(TName.test, testMutexEntity);
        System.out.println(commitResult.getReuslt());
    }
}
