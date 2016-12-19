package com.redis.transaction.lock;

import com.redis.log.Loggers;
import com.redis.transaction.enums.TLockState;
import com.redis.transaction.exception.TException;
import com.redis.transaction.service.RedisService;
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
    private RedisService redisService;
    private String tEntityName;
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


    public TLockImpl(String lockKey, RedisService redisService, String tEntityName) {
        this(lockKey, redisService, tEntityName, TimeUtil.MINUTE_SECOND, false);
    }

    public TLockImpl(String lockKey, RedisService redisService, String tEntityName, long lockTime, boolean forceFlag, String lockContent) {
        this(lockKey, redisService, tEntityName, lockTime, forceFlag);
        this.lockContent = lockContent;
    }

    public TLockImpl(String lockKey, RedisService redisService, String tEntityName, long lockTime, boolean forceFlag) {
        super();
        this.lockKey = lockKey;
        this.redisService = redisService;
        this.tEntityName = tEntityName;
        this.lockState = TLockState.init;
        this.lockTime = lockTime;
        this.forceFlag = forceFlag;
    }


    @Override
    public void destroy() {
        //初始化状态不需要销毁
        if (this.lockState.equals(TLockState.init) || this.lockState.equals(TLockState.create)) {
            return;
        }

        boolean deleteFlag = true;
        if (!StringUtils.isEmptyString(lockContent)) {
            deleteFlag = checkContent();
        }
        if (deleteFlag) {
            boolean flag = redisService.deleteKey(getLockKey(lockKey, tEntityName));
            if (!flag) {
                logger.info("TLockImpl" + lockKey + ":tEntityName" + tEntityName.toString() + "destroy is error");
            }
        }
    }

    @Override
    public boolean create(long seconds) throws TException {
        this.lockState = TLockState.create;
        boolean createFlag = false;

        try {

            String realLockKey = getLockKey(lockKey, tEntityName);
            logger.info("realLockKey:" + realLockKey);
            createFlag = redisService.setNxString(realLockKey, lockContent, getLockTime());
            if (createFlag) {
                logger.info("create realLockKey:" + realLockKey + ",time: " + getLockTime());
                this.lockState = TLockState.success;
                redisService.expire(realLockKey, getLockTime());
            } else {
                if (forceFlag) {
                    this.lockState = TLockState.success;
                    redisService.setString(realLockKey, lockContent, getLockTime());
                    redisService.expire(realLockKey, getLockTime());
                    createFlag = true;
                    logger.info("forece create realLockKey:" + realLockKey + ",time: " + getLockTime());
                } else {
                    logger.info("create error realLockKey:" + realLockKey + ",time: " + getLockTime());
                }
            }
        } catch (Exception e) {
            throw new TException(e.toString());
        }

        return createFlag;
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
        String content = redisService.getString(getLockKey(lockKey, tEntityName));
        return !StringUtils.isEmptyString(content) ? content.equals(this.lockContent) : false;
    }


    /**
     * 获取锁Key
     *
     * @param lockKey
     * @param GameTransactionEntityCause
     * @return
     */
    public String getLockKey(String lockKey, String GameTransactionEntityCause) {
        return lockKey + "#" + tEntityName.toString();
    }
    /**
     * 获取信息
     *
     * @return
     */
    public String getInfo() {
        return lockKey + StringUtils.DEFAULT_SPLIT + tEntityName.toString() + StringUtils.DEFAULT_SPLIT + this.lockState;
    }


}

