package com.redis.transaction.read;

import com.redis.transaction.TName;
import com.redis.transaction.TEntityName;
import com.redis.transaction.TEntityFactoryImpl;
import com.redis.transaction.RedisKey;
import com.redis.transaction.db.DBConfigManager;
import com.redis.transaction.job.entity.TJobEnityImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.job.TransactionJob;
import com.redis.transaction.job.TransactionJobImpl;

/**
 * Created by jiangwenping on 16/12/7.
 */
public class TestReadTransaction {
    public static void main(String[] args) throws Exception {

        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(DBConfigManager.getJedisPool());//绑定redis数据源
        TransactionJob job = new TransactionJobImpl();

        String uid = "#playerId";

        TJobEnityImpl edit_a = TEntityFactoryImpl.createReadTEnity(redisService, RedisKey.lock_pre_common, TEntityName.EDIT_A + uid);
        TJobEnityImpl edit_b = TEntityFactoryImpl.createReadTEnity(redisService, RedisKey.lock_pre_common, TEntityName.EDIT_B + uid);

        CommitResult commitResult = job.commit(TName.read, edit_b, edit_a);

        System.out.println(commitResult.getReuslt());

        TJobEnityImpl commonRejectReadTransactionEnity = TEntityFactoryImpl.createReadRejectTEnity(redisService, RedisKey.lock_pre_common, TEntityName.EDIT_B + uid);
        commitResult = job.commit(TName.test, commonRejectReadTransactionEnity);
        System.out.println(commitResult.getReuslt());
    }
}
