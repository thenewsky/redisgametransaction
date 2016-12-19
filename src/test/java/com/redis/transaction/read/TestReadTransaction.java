package com.redis.transaction.read;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.entity.TEnityImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.service.ConfigService;
import com.redis.transaction.service.RedisService;
import com.redis.transaction.service.TransactionService;
import com.redis.transaction.service.TransactionServiceImpl;

/**
 * Created by jiangwenping on 16/12/7.
 */
public class TestReadTransaction {
    public static void main(String[] args) throws Exception {


        RedisService redisService = new RedisService();
        redisService.setJedisPool(ConfigService.getJedisPool());//绑定redis数据源

        TransactionService transactionService = new TransactionServiceImpl();
        String union = "union";
        TEnityImpl readTEnity = TEntityFactoryImpl.createReadTEnity(TEntityName.read, redisService, RedisKey.common, union);
        TEnityImpl readTEnity1 = TEntityFactoryImpl.createReadTEnity(TEntityName.test, redisService, RedisKey.common, union);
        CommitResult commitResult = transactionService.commitTransaction(TName.read, readTEnity,readTEnity1);
        System.out.println(commitResult.getReuslt());

//        TEnityImpl commonRejectReadTransactionEnity = TEntityFactoryImpl.createReadRejectTEnity(TEntityName.read, redisService, RedisKey.common, union);
//        commitResult = transactionService.commitTransaction(TName.read, commonRejectReadTransactionEnity);
//        System.out.println(commitResult.getReuslt());
    }
}
