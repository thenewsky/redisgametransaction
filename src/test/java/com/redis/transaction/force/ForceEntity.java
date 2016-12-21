package com.redis.transaction.force;

import com.redis.transaction.db.RedisDao;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.job.entity.AbstractTJobEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.exception.TException;

/**
 * Created by jiangwenping on 16/12/7.
 */
public class ForceEntity extends AbstractTJobEntity {

    public ForceEntity(RedisDao RedisDao, String key_pre, String entity_name, TLockType type, int seconds) {
        super(RedisDao, key_pre, entity_name, type, seconds);
    }

    @Override
    public void commit() throws TException {

    }

    @Override
    public void rollback() throws TException {

    }

    @Override
    public CommitResult trycommit() throws TException {
        return CommitResult.SUCCESS;
    }
}
