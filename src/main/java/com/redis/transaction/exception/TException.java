package com.redis.transaction.exception;

import com.redis.transaction.enums.CommitResult;

/**
 * Created by jiangwenping on 16/11/26.
 * 事务锁运行时异常
 */
public class TException extends RuntimeException {

    /**
     * 事务执行出错原因
     */
    private CommitResult commitResult;

    public TException() {
        super();
    }

    public TException(CommitResult commitResult) {
        super();
        this.commitResult = commitResult;
    }

    public TException(String msg) {
        super(msg);
    }

    public CommitResult getCommitResult() {
        return commitResult;
    }

}
