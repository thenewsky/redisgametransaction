package com.redis.transaction.enums;

/**
 * Created by jiangwenping on 16/11/26.
 * 事务锁状态
 */
public enum TLockState {
    /**
     * 初始化
     */
    INIT,

    /** 锁定中 */
    LOCKING,

    /**lockAll 创建成功*/
    LOCKED,
}
