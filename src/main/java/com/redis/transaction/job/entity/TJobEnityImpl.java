package com.redis.transaction.job.entity;

import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.exception.TException;

/**
 * Created by jiangwenping on 16/12/7.
 * 读取实体
 */
public class TJobEnityImpl extends AbstractTJobEntity {

    public TJobEnityImpl(String cause, String key, RedisDaoImpl redisService) {
        super(cause, key, redisService, TLockType.READ);
    }


    @Override
    public void commit() throws TException {

    }

    @Override
    public void rollback() throws TException {

    }

    @Override
    public CommitResult trycommit()
            throws TException {
        return CommitResult.SUCCESS;
    }

    @Override
    public String getInfo() {
        return "TEnity-getInfo";
    }

}
