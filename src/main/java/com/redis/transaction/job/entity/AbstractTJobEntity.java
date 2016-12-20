package com.redis.transaction.job.entity;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.enums.TLockType;
import com.redis.transaction.exception.TException;
import com.redis.transaction.lock.TLockImpl;
import com.redis.transaction.lock.TLock;
import com.redis.transaction.lock.TReadLockImpl;
import org.slf4j.Logger;

import java.util.BitSet;

/**
 * Created by jiangwenping on 16/11/26.
 * 抽象事务实体
 */
public abstract class AbstractTJobEntity implements TJobEntity {

    protected static Logger transactionLogger = Loggers.transactionLogger;

    /**
     * 进度设置集合 主要用于rollback
     */
    private BitSet progressBitSet;

    /**
     * 事务锁
     */
    private TLock tLock;

    /**
     * 锁类型
     */
    private TLockType tLockType;

    /**
     * 锁的正向标志(主要用于读取的时候)
     */
    private boolean rejectFlag = false;

    //defult
    public AbstractTJobEntity(String cause, String key, RedisDaoImpl redisService) {
        this(cause, key, redisService, TLockType.WRITE);
    }

    public AbstractTJobEntity(String cause, String key, RedisDaoImpl redisService, TLockType gameTransactionLockType) {
        this.progressBitSet = new BitSet();
        this.tLock = new TLockImpl(key, redisService, cause);
        transactionLogger.debug("<debug-add-entity>" + "AbstractTJobEntity" + "this.tLock = new TLockImpl(key, redisService, cause);");
        this.tLockType = gameTransactionLockType;
    }


    /**
     * @param cause
     * @param key
     * @param redisService
     * @param tLockType
     * @param lockTime     此参数只针对 非readlock锁
     */
    public AbstractTJobEntity(String cause, String key, RedisDaoImpl redisService, TLockType tLockType, long lockTime) {
        this.progressBitSet = new BitSet();
        this.tLockType = tLockType;

        if (tLockType.equals(TLockType.READ)) {
            this.tLock = new TReadLockImpl(key, redisService, cause);
        } else if (tLockType.equals(TLockType.FORCE_WRITE_TIME)) {
            this.tLock = new TLockImpl(key, redisService, cause, lockTime, true);
        } else {
            this.tLock = new TLockImpl(key, redisService, cause, lockTime, false);

        }

    }

    /**
     * 设置提交事务的提交进度，用于回滚
     *
     * @param step
     */
    public void setCommitProgress(int step) throws TException {
        if (checkCommitProgress(step)) {
            throw new TException("progress has exsited");
        }
        progressBitSet.set(step);
    }

    /**
     * 检测当前进度是否设置过
     *
     * @param step
     * @return
     */
    public boolean checkCommitProgress(int step) {
        return progressBitSet.get(step);
    }

    /**
     * 清除进度
     *
     * @param step
     */
    public void cleanCommitProgress(int step) {
        progressBitSet.set(step, false);
    }

    @Override
    public boolean lock(long uuid) throws TException {
        boolean result = tLock.lock(uuid);
        if (rejectFlag) {
            result = !result;
        }
        return result;
    }

    @Override
    public void unlock() {
        //时间占有锁，不释放锁
        if (tLockType == TLockType.WRITE_TIME || tLockType == TLockType.FORCE_WRITE_TIME) {
            return;
        }
        tLock.unLock();
    }

    @Override
    public void forceRelease() {
        tLock.unLock();
    }


    /**
     * 是否需要执行
     *
     * @return
     */
    public boolean needCommit() {
        return !tLockType.equals(TLockType.READ);

    }

    public boolean isRejectFlag() {
        return rejectFlag;
    }

    public void setRejectFlag(boolean rejectFlag) {
        this.rejectFlag = rejectFlag;
    }

    public TLock getLock() {
        return this.tLock;
    }

    public String getInfo() {
        return tLockType + "类型" + tLock.getInfo();
    }
}
