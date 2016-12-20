package com.redis.transaction.entity;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.service.ConfigService;
import com.redis.transaction.service.TransactionService;
import com.redis.transaction.service.TransactionServiceImpl;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class TestMutexTransaction {
    public static void main(String[] args) throws Exception {

        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(ConfigService.getJedisPool());

        TransactionService transactionService = new TransactionServiceImpl();
        String union = "union";
        TestMutexEntity testMutexEntity = TEntityFactoryImpl.createTestMutexEntity(TEntityName.test, redisService, RedisKey.player,  union);
        CommitResult commitResult = transactionService.commitTransaction(TName.test, testMutexEntity);
        System.out.println(commitResult.getReuslt());
    }
}
