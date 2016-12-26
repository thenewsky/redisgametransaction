package com.redis.transaction.lock;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.db.RedisKeyUtil;
import com.redis.transaction.enums.TLockState;
import com.redis.transaction.exception.TException;
import com.redis.transaction.db.DBDao;
import com.redis.util.StringUtils;
import com.redis.util.TimeUtil;
import org.slf4j.Logger;

/**
 * Created by jiangwenping on 16/11/26
 * 写锁
 */
public class TLockImpl implements TLock {
    protected static Logger logger = Loggers.lockLogger;
    /**
     * 对应的锁key
     */
    private String lockKey;
    /**
     * redis
     */
    private DBDao dbDao;
    private String name;
    /**
     * 锁定状态
     */
    private TLockState lockState;
    /**
     * 锁时间 单位为秒
     */
    private long lockTime;
    /**
     * 强制标志
     */
    private boolean forceFlag;
    /**
     * 锁内容
     */
    private String lockContent = "";


    public TLockImpl(String lockKey, DBDao redisService, String name) {
        this(lockKey, redisService, name, TimeUtil.MINUTE_SECOND, false);
    }

    public TLockImpl(String lockKey, RedisDaoImpl redisService, String tEntityName, long lockTime, boolean forceFlag, String lockContent) {
        this(lockKey, redisService, tEntityName, lockTime, forceFlag);
        this.lockContent = lockContent;
    }

    public TLockImpl(String lockKey, DBDao redisService, String name, long lockTime, boolean forceFlag) {
        super();
        this.lockKey = lockKey;
        this.dbDao = redisService;
        this.name = name;
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
            boolean flag = dbDao.deleteKey(RedisKeyUtil.getLockKey(lockKey, name));
            if (!flag) {
                logger.info("TLockImpl" + lockKey + ":name" + name + "unLock is error");
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
        return lockKey + StringUtils.DEFAULT_SPLIT + name.toString() + StringUtils.DEFAULT_SPLIT + this.lockState;
    }

    @Override
    public boolean lock(long uuid) throws TException {
        this.lockState = TLockState.LOCKING;
        boolean isLocked = false;
        try {
            String realLockKey = RedisKeyUtil.getLockKey(lockKey, name);
            logger.info("realLockKey:" + realLockKey);
            isLocked = dbDao.setNxString(realLockKey, lockContent, getLockTime());
            if (isLocked) {
                logger.info("lockAll realLockKey:" + realLockKey + ",time: " + getLockTime());
                this.lockState = TLockState.LOCKED;
            } else {
                if (forceFlag) {
                    this.lockState = TLockState.LOCKED;
                    dbDao.setString(realLockKey, lockContent, getLockTime());//强得锁
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
        String content = dbDao.getString(RedisKeyUtil.getLockKey(lockKey, name));
        return !StringUtils.isEmptyString(content) ? content.equals(this.lockContent) : false;
    }


}

