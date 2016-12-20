package com.redis.transaction.force;

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
 * Created by jiangwenping on 16/12/7.
 */
public class FroceTest {
    public static void main(String[] args) throws Exception {
        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(DBConfigManager.getJedisPool());

        TransactionJob transactionService = new TransactionJobImpl();

        String union = "union";
        String attchMent = "attchement";
        ForceEntity forceEntity = TEntityFactoryImpl.createForceEntity(TEntityName.force, redisService, RedisKey.player, union, TimeUtil.SIX_HOUR_SECOND);
        forceEntity.getLock().setContent(attchMent);
        CommitResult commitResult = transactionService.commit(TName.force, forceEntity);
        System.out.println(commitResult.getReuslt());
    }
}
