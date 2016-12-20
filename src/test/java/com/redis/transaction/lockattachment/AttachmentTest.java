package com.redis.transaction.lockattachment;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.job.entity.TestMutexEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.db.DBConfigManager;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.job.TransactionJob;
import com.redis.transaction.job.TransactionJobImpl;

/**
 * Created by jiangwenping on 16/12/7.
 */
public class AttachmentTest {

    public static void main(String[] args) throws Exception {
        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(DBConfigManager.getJedisPool());

        TransactionJob transactionService = new TransactionJobImpl();

        String union = "union";
        String attchMent = "attchement";
        TestMutexEntity testMutexEntity = TEntityFactoryImpl.createTestMutexEntity(TEntityName.attchment, redisService, RedisKey.player, union);
        testMutexEntity.getLock().setContent(attchMent);
        CommitResult commitResult = transactionService.commit(TName.attchment, testMutexEntity);
        System.out.println(commitResult.getReuslt());

//        TJobEnityImpl commonReadTransactionEnity = TEntityFactoryImpl.createReadTEnity(TEntityName.attchment, redisService, RedisKey.player, union);
//        commonReadTransactionEnity.getLock().setContent(attchMent);
//        commitResult = transactionService.commit(TName.EDIT_B, commonReadTransactionEnity);
//        System.out.println(commitResult.getReuslt());
    }
}
