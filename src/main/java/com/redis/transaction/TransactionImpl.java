package com.redis.transaction;

import com.redis.transaction.job.entity.TJobEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import com.redis.util.TimeUtil;

/**
 * 事务合集用于调度 all entity
 */
public class TransactionImpl extends AbstractTransaction {

    private long waitTime;

    public TransactionImpl(String cause) {
        super(cause);
    }

    public TransactionImpl(String cause, long waitTime) {
        super(cause);
        this.waitTime = waitTime;
    }


    public void commit() throws TException {
        if (state != TRY_COMMITED)
            throw new TException();
        this.state = COMMITED;
        for (TJobEntity entity : entities) {
            if (!entity.needCommit()) {
                continue;
            }
            entity.commit();
        }
    }

    public void rollback() throws TException {
        //无条件进行回滚
        this.state = ROLLBACK;
        for (TJobEntity entity : entities) {
            entity.rollback();
        }
    }


    @Override
    public void trycommit() throws TException {
        if (state != ACTIVE)
            throw new TException("TransactionImpl is active, can't trycommit");
        this.state = TRY_COMMITED;
        for (TJobEntity entity : entities) {
            if (!entity.needCommit()) {
                continue;
            }
            CommitResult gameTransactionEntityTryCommitResult = entity.trycommit();
            if (!gameTransactionEntityTryCommitResult.equals(CommitResult.SUCCESS)) {
                this.commitResult = gameTransactionEntityTryCommitResult;
                break;
            }
        }
    }


    @Override
    public boolean lockAll() throws TException {
        if (state != ACTIVE)
            throw new TException();
        boolean creatflag = true;
        long startSecond = TimeUtil.getSeconds();
        if (waitTime > 0) {
            for (; ; ) {
                long uuid = TimeUtil.getSeconds();//lock 唯一id  粒度 自定义.目前为秒
                creatflag = lockAllEntities(uuid);
                if (creatflag = true) {
                    break;
                }

                try {
                    Thread.sleep(TimeUtil.SECOND);
                } catch (Throwable e) {

                }

                uuid = TimeUtil.getSeconds();
                if (startSecond + waitTime < uuid) {
                    creatflag = false;
                    break;
                }
            }
        } else {
            long seconds = TimeUtil.getSeconds();
            creatflag = lockAllEntities(startSecond);
        }
        return creatflag;
    }

    public boolean lockAllEntities(long uuid) throws TException {
        boolean creatFlag = false;
        for (TJobEntity entity : entities) {
            try {
                creatFlag = entity.lock(uuid);
            } catch (Exception e) {
                throw new TException(e.getMessage());
            }
            if (!creatFlag) {
                break;
            }
        }
        return creatFlag;
    }


    @Override
    public void unlockAll() {
        for (TJobEntity entity : entities) {
            entity.unlock();
        }
    }


    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("transaction ");
        buffer.append(getName());
        buffer.append(":");
        for (int i = 0; i < entities.size(); i++) {
            TJobEntity entity = entities.get(i);
            buffer.append(entity.getInfo());
            if (i < entities.size() - 1) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}


