package com.redis.transaction;

/**
 * Created by jiangwenping on 16/12/6.
 */

import com.redis.transaction.entity.TEntity;
import com.redis.transaction.enums.CommitResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author C172
 *         抽象游戏事务
 */
public abstract class AbstractTransaction implements GameTransaction {

    /**
     * 当前执行状态
     */
    protected int state;
    /**
     * 事务实体
     */
    protected List<TEntity> entities;
    /**
     * 事务原因
     */
    private String cause;

    /**
     * 游戏事务提交结果
     */
    protected CommitResult gameTransactionTryCommitResult;

    public AbstractTransaction(String cause) {
        this.cause = cause;
        this.entities = new ArrayList<TEntity>();
        gameTransactionTryCommitResult = CommitResult.SUCCESS;
        this.state = ACTIVE;
    }

    public void addEntity(TEntity entity) {
        entities.add(entity);
    }

    @Override
    public String getCause() {
        return cause;
    }

    @Override
    public boolean canCommit() {
        return gameTransactionTryCommitResult.equals(CommitResult.SUCCESS);
    }

    public CommitResult getGameTransactionTryCommitResult() {
        return gameTransactionTryCommitResult;
    }


}

