package com.redis.transaction.lock;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.TLockState;
import com.redis.transaction.exception.TException;
import com.redis.util.StringUtils;
import org.slf4j.Logger;

/**
 * Created by jiangwenping on 16/11/26.
 * 读锁
 */
public class TReadLockImpl implements TLock {

    protected static Logger logger = Loggers.lockLogger;

    /**
     * 对应的锁key
     */
    private String lockKey;

    /**
     * redis
     */
    private RedisDaoImpl redisService;

    /**
     * 事务原因
     */
    private String tName;

    /**
     * 锁定状态
     */
    private TLockState lockState;

    /**
     * 锁内容
     */
    private String lockContent = "";

    public TReadLockImpl(String lockKey, RedisDaoImpl redisService, String gameTransactionEntityCause) {
        super();
        this.lockKey = lockKey;
        this.redisService = redisService;
        this.tName = gameTransactionEntityCause;
        this.lockState = TLockState.INIT;
    }

    public TReadLockImpl(String lockKey, String gameTransactionEntityCause) {
        super();
        this.lockKey = lockKey;
        this.tName = gameTransactionEntityCause;
        this.lockState = TLockState.INIT;
    }

    @Override
    public void unLock() {

    }

    @Override
    public boolean lock(long seconds) throws TException {
        this.lockState = TLockState.LOCKING;
        String realLockKey = getLockKey(lockKey, tName);
        logger.info("read  realLockKey:" + realLockKey);
        boolean detectflag = redisService.exists(realLockKey);
        if (detectflag && !StringUtils.isEmptyString(this.lockContent)) {
            detectflag = checkContent();
        }
        return detectflag;
    }

    @Override
    public String getInfo() {
        return lockKey + StringUtils.DEFAULT_SPLIT + tName.toString() + StringUtils.DEFAULT_SPLIT + this.lockState;
    }


    /**
     * 获取锁可以
     *
     * @param lockKey
     * @param GameTransactionEntityCause
     * @return
     */
    public String getLockKey(String lockKey, String GameTransactionEntityCause) {
        return lockKey + "#" + tName;
    }

    @Override
    public void setContent(String lockContent) {
        this.lockContent = lockContent;
    }

    /**
     * 检查锁内容
     *
     * @return
     */
    public boolean checkContent() {
        boolean checkFlag = false;
        String realLockKey = getLockKey(lockKey, tName);
        String content = redisService.getString(realLockKey);
        if (!StringUtils.isEmptyString(content)) {
            logger.info("read content realLockKey:" + realLockKey);
            checkFlag = content.equals(this.lockContent);
        }

        return checkFlag;
    }

}
