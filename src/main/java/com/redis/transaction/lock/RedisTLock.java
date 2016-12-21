package com.redis.transaction.lock;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDao;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.db.RedisKeyUtil;
import com.redis.transaction.enums.TLockState;
import com.redis.transaction.exception.TException;
import com.redis.util.StringUtils;
import com.redis.util.TimeUtil;
import org.slf4j.Logger;

/**
 * Created by jiangwenping on 16/11/26
 * 写锁
 */
public abstract class RedisTLock implements TLock {


    protected static Logger logger = Loggers.lockLogger;

    protected String key_pre;
    protected String transaction_name;
    protected String entity_name;

    protected RedisDao redisDao;

    protected TLockState lockState;

    protected long lockTime;

    protected String lockContent = "";

    protected boolean forceFlag;

    public RedisTLock(RedisDao redisDao, String key_pre, String entity_name) {
        this(redisDao, key_pre, entity_name, TimeUtil.MINUTE_SECOND, false);
    }

    public RedisTLock(RedisDao redisDao, String key_pre, String entity_name, long lockTime, boolean forceFlag, String lockContent) {
        this(redisDao, key_pre, entity_name, lockTime, forceFlag);
        this.lockContent = lockContent;
    }

    public RedisTLock(RedisDao redisDao, String key_pre, String name, long lockTime, boolean forceFlag) {
        super();
        this.redisDao = redisDao;
        this.key_pre = key_pre;
        this.entity_name = name;
        this.lockState = TLockState.INIT;
        this.lockTime = lockTime;
        this.forceFlag = forceFlag;
    }


    @Override
    public void unLock() {
        //初始化状态不需要销毁
        if (this.lockState.equals(TLockState.INIT) || this.lockState.equals(TLockState.LOCKING)) {
            return;
        }

        boolean deleteFlag = true;
        if (!StringUtils.isEmptyString(lockContent)) {
            deleteFlag = checkContent();
        }
        if (deleteFlag) {
            boolean flag = redisDao.deleteKey(RedisKeyUtil.getLockKey(key_pre, transaction_name, entity_name));
            if (!flag) {
                logger.info("TLockImpl" + key_pre + ":entity_name" + entity_name + "unLock is error");
            }
        }
    }

    @Override
    /**
     * 获取信息
     *
     * @return
     */
    public String getInfo() {
        return key_pre + StringUtils.DEFAULT_SPLIT + entity_name.toString() + StringUtils.DEFAULT_SPLIT + this.lockState;
    }

    @Override
    public boolean lock(String transction_name) throws TException {
        this.lockState = TLockState.LOCKING;
        this.transaction_name = transction_name;
        boolean isLocked = false;
        try {
            String realLockKey = RedisKeyUtil.getLockKey(key_pre, transction_name, entity_name);
            logger.info("realLockKey:" + realLockKey);
            isLocked = redisDao.setNxString(realLockKey, lockContent, getLockTime());
            if (isLocked) {
                logger.info("lockAll realLockKey:" + realLockKey + ",time: " + getLockTime());
                this.lockState = TLockState.LOCKED;
            } else {
                if (forceFlag) {
                    this.lockState = TLockState.LOCKED;
                    redisDao.setString(realLockKey, lockContent, getLockTime());//强得锁
                    isLocked = true;
                    logger.info("forece lockAll realLockKey:" + realLockKey + ",time: " + getLockTime());
                } else {
                    logger.info("lockAll error realLockKey:" + realLockKey + ",time: " + getLockTime());
                }
            }
        } catch (Exception e) {
            throw new TException(e.toString());
        }

        return isLocked;
    }


    /**
     * 获取锁定时间
     *
     * @return
     */
    public int getLockTime() {
        return (int) this.lockTime;
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
        String content = getLockContent();
        return !StringUtils.isEmptyString(content) ? content.equals(this.lockContent) : false;
    }


    protected String getLockContent() {
        return redisDao.getString(getReadLockKey());
    }

    protected String getReadLockKey() {
        return RedisKeyUtil.getLockKey(key_pre, transaction_name, entity_name);
    }

}

