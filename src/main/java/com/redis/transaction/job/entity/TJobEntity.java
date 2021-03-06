package com.redis.transaction.job.entity;

import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import com.redis.transaction.lock.TLock;

/**
 * Created by jiangwenping on 16/11/26.
 * 游戏事务实体接口
 */
public interface TJobEntity {

    /**
     * 执行
     */
    public void commit() throws TException;

    /**
     * 回滚
     */
    public void rollback() throws TException;

    /**
     * 尝试性提交
     */
    public CommitResult trycommit() throws TException;


    /**
     * 是否可以创建锁
     *
     * @return
     */
    public boolean lock(String transction_name) throws TException;

    /**
     * 释放锁
     *
     * @return
     */
    public void unlock();

    /**
     * 强制释放锁
     *
     * @return
     */
    public void forceRelease();

    public String getInfo();

    /**
     * 是否需要执行
     *
     * @return
     */
    public boolean needCommit();

    /**
     * 获取锁内容
     *
     * @return
     */
    public TLock getLock();
}

