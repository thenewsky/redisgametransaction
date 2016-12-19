package com.redis.transaction.lock;

import com.redis.transaction.exception.TException;

/**
 * Created by jiangwenping on 16/11/26
 * 事务接口
 */
public interface TLock {
    /**
     * 销毁
     */
    public void destroy();

    /**
     * 创建
     * @return
     */
    public boolean create(long seconds)  throws TException;

    /**
     * 获取信息
     * @return
     */
    public String getInfo();

    /**
     * 设置内容
     */
    public void setContent(String lockContent);

}

