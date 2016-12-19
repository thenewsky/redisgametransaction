package com.redis.transaction.lockattachment;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.entity.TestMutexEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.service.ConfigService;
import com.redis.transaction.service.RedisService;
import com.redis.transaction.service.TransactionService;
import com.redis.transaction.service.TransactionServiceImpl;

/**
 * Created by jiangwenping on 16/12/7.
 */
public class AttachmentTest {

    public static void main(String[] args) throws Exception {
        RedisService redisService = new RedisService();
        redisService.setJedisPool(ConfigService.getJedisPool());

        TransactionService transactionService = new TransactionServiceImpl();

        String union = "union";
        String attchMent = "attchement";
        TestMutexEntity testMutexEntity = TEntityFactoryImpl.createTestMutexEntity(TEntityName.attchment, redisService, RedisKey.player, union);
        testMutexEntity.getLock().setContent(attchMent);
        CommitResult commitResult = transactionService.commitTransaction(TName.attchment, testMutexEntity);
        System.out.println(commitResult.getReuslt());

//        TEnityImpl commonReadTransactionEnity = TEntityFactoryImpl.createReadTEnity(TEntityName.attchment, redisService, RedisKey.player, union);
//        commonReadTransactionEnity.getLock().setContent(attchMent);
//        commitResult = transactionService.commitTransaction(TName.read, commonReadTransactionEnity);
//        System.out.println(commitResult.getReuslt());
    }
}
