package com.redis.transaction;

import com.redis.transaction.exception.TException;

/**
 * Created by jiangwenping on 16/12/6.
 */
public interface GameTransaction {

    /**
     * 激活，构造
     */
    public static final int ACTIVE = 0;
    /**
     * 尝试提交
     */
    public static final int TRY_COMMITED = 1;
    /**
     * 正式提交
     */
    public static final int COMMITED = 2;
    /**
     * 正式回滚
     */
    public static final int ROLLBACK = 3;

    /**
     * 事务提交
     *
     * @throws TException
     */
    public void commit() throws TException;

    /**
     * 事务回滚
     *
     * @throws TException
     */
    public void rollback() throws TException;

    /**
     * 是否可以提交
     *
     * @return
     */
    public boolean canCommit();

    /**
     * 尝试性提交
     */
    public void trycommit() throws TException;

    /**
     * 获取事务原因
     *
     * @return
     */
    public String getCause();

    /**
     *
     * [
     * entity1_lock
     * entity2_lock
     * ....
     * entityn_lock
     * ]
     *
     * @return
     */
    public boolean lockAll() throws TException;


    /**
     *
     * [
     * entity1_unlock
     * entity2_unlock
     * ....
     * entityn_unlock
     * ]
     *
     * @return
     */
    public void unlockAll();
}
