package com.redis.transaction.lock;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.db.RedisKeyUtil;
import com.redis.transaction.enums.TLockState;
import com.redis.transaction.exception.TException;
import com.redis.transaction.db.RedisDao;
import com.redis.util.StringUtils;
import com.redis.util.TimeUtil;
import org.slf4j.Logger;

/**
 * Created by jiangwenping on 16/11/26
 * 写锁
 */
public class TLockImpl extends RedisTLock {


    public TLockImpl(RedisDao redisDao, String key_pre, String name) {
        super(redisDao, key_pre, name);
    }

    public TLockImpl(RedisDao redisDao, String key_pre, String name, long lockTime, boolean forceFlag) {
        super(redisDao, key_pre, name, lockTime, forceFlag);
    }

    public TLockImpl(RedisDao redisDao, String key_pre, String entity_name, long lockTime, boolean forceFlag, String lockContent) {
        super(redisDao, key_pre, entity_name, lockTime, forceFlag, lockContent);
    }


}

