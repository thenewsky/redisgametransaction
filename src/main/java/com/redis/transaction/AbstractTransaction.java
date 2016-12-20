package com.redis.transaction;

/**
 * Created by jiangwenping on 16/12/6.
 */

import com.redis.transaction.job.entity.TJobEntity;
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
     * 事务实体列表
     */
    protected List<TJobEntity> entities;
    /**
     * 事务名称
     */
    private String name;

    /**
     * 游戏事务提交结果
     */
    protected CommitResult commitResult;

    public AbstractTransaction(String name) {
        this.name = name;
        this.entities = new ArrayList<TJobEntity>();
        commitResult = CommitResult.SUCCESS;
        this.state = ACTIVE;
    }

    public void addEntity(TJobEntity entity) {
        entities.add(entity);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean canCommit() {
        return commitResult == CommitResult.SUCCESS;
    }

    public CommitResult getCommitResult() {
        return commitResult;
    }


}

