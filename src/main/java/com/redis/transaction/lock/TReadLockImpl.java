package com.redis.transaction.lock;

import com.redis.transaction.db.RedisDao;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.TLockState;
import com.redis.transaction.exception.TException;
import com.redis.util.StringUtils;


public class TReadLockImpl extends RedisTLock {


    public TReadLockImpl(RedisDao redisDao, String key_pre, String name) {
        super(redisDao, key_pre, name);
    }

    public TReadLockImpl(RedisDaoImpl redisDao, String lockKey, String tEntityName, long lockTime, boolean forceFlag, String lockContent) {
        super(redisDao, lockKey, tEntityName, lockTime, forceFlag, lockContent);
    }

    public TReadLockImpl(RedisDao redisDao, String lockKey, String name, long lockTime, boolean forceFlag) {
        super(redisDao, lockKey, name, lockTime, forceFlag);
    }

    @Override
    public void unLock() {
    }

    @Override
    public boolean lock(String transction_name) throws TException {

        this.lockState = TLockState.LOCKING;

        String realLockKey = getReadLockKey();
        logger.info("read  realLockKey:" + realLockKey);

        boolean detectflag = redisDao.exists(realLockKey);

        if (detectflag && !StringUtils.isEmptyString(this.lockContent)) {
            detectflag = checkContent();
        }
        return detectflag;
    }


}
