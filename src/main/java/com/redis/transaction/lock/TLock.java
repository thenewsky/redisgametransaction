package com.redis.transaction.lock;

import com.redis.transaction.exception.TException;

/**
 * Created by jiangwenping on 16/11/26
 * 事务接口
 */
public interface TLock {


    /**
     * 依赖DB创建的锁
     * @return
     */
    public boolean lock(long seconds)  throws TException;
    public void unLock();
//    /**
//     * 获取信息
//     * @return
//     */
//    public String getInfo();
    /**
     * 设置内容
     */
    public void setContent(String lockContent);

}

