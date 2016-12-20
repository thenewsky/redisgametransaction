package com.redis.transaction.enums;

/**
 * Created by jiangwenping on 16/11/26.
 * 事务执行结果
 */
public enum CommitResult {

    /**
     * 成功
     */
    SUCCESS("LOCKED"),
    /**
     * 失败
     */
    COMMON_ERROR("common_error"),
    /**
     * 失败
     */
    LOCK_ERROR("lock_error");


    /**
     * 事务执行结果
     */
    private String reuslt;
    private CommitResult() {}
    private CommitResult(String reuslt) {
        this.reuslt = reuslt;
    }
    public String getReuslt() {
        return reuslt;
    }
}
