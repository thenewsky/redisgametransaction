package com.redis.transaction.service;

import com.redis.transaction.entity.AbstractTEntity;
import com.redis.transaction.enums.CommitResult;

/**
 * Created by jiangwenping on 16/11/26.
 * 提交事务，允许多个实体
 */
public interface TransactionService {
    /**
     * 提交事务
     *
     * @return
     */
    public CommitResult commitTransaction(String name, AbstractTEntity... abstractGameTransactionEntity);

    /**
     * 提交事务
     *
     * @return
     */
    public CommitResult commitTransaction(String name, long waitTime, AbstractTEntity... abstractGameTransactionEntity);
}
