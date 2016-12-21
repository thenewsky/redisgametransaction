package com.redis.transaction.wait;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.db.DBConfigManager;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.job.TransactionJob;
import com.redis.transaction.job.TransactionJobImpl;
import com.redis.util.TimeUtil;

/**
 * Created by jiangwenping on 16/12/15.
 */
public class WaitMutexTransaction {
    public static void main(String[] args) throws Exception {

        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(DBConfigManager.getJedisPool());

        TransactionJob transactionService = new TransactionJobImpl();
        String union = "union";
        long waitTime = TimeUtil.MINUTE_SECOND;
        WaitMutexEntity waitMutexTransaction = TEntityFactoryImpl.createWaitMutexEntity(redisService,TEntityName.wait,  RedisKey.player, union);
        CommitResult commitResult = transactionService.commit(TName.wait, waitTime, waitMutexTransaction);
        System.out.println(commitResult.getReuslt());
    }
}
