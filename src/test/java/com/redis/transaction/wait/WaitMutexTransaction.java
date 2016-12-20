package com.redis.transaction.wait;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.service.ConfigService;
import com.redis.transaction.service.TransactionService;
import com.redis.transaction.service.TransactionServiceImpl;
import com.redis.util.TimeUtil;

/**
 * Created by jiangwenping on 16/12/15.
 */
public class WaitMutexTransaction {
    public static void main(String[] args) throws Exception {

        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(ConfigService.getJedisPool());

        TransactionService transactionService = new TransactionServiceImpl();
        String union = "union";
        long waitTime = TimeUtil.MINUTE_SECOND;
        WaitMutexEntity waitMutexTransaction = TEntityFactoryImpl.createWaitMutexEntity(TEntityName.wait, redisService, RedisKey.player, union);
        CommitResult commitResult = transactionService.commitTransaction(TName.wait, waitTime, waitMutexTransaction);
        System.out.println(commitResult.getReuslt());
    }
}
