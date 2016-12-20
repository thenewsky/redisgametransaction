package com.redis.transaction.job;
import com.redis.transaction.job.entity.TJobEntity;
import com.redis.transaction.enums.CommitResult;

/**
 *
 * 一次事务提交job:
 * job_entity_1
 * job_entity_2
 * job_entity_3
 * .....
 * job_entity_n
 */
public interface TransactionJob {
    /**
     * 提交事务
     *
     * @return
     */
    public CommitResult commit(String name, TJobEntity... jobEntities);

    /**
     * 提交事务
     *
     * @return
     */
    public CommitResult commit(String name, long waitTime, TJobEntity... jobEntities);
}
