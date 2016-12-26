package com.redis.transaction.factory;

import com.redis.log.Loggers;
import com.redis.transaction.db.DBDao;
import com.redis.transaction.db.RedisDaoImpl;
import com.redis.transaction.db.RedisKeyUtil;
import com.redis.transaction.job.entity.TJobEnityImpl;
import org.slf4j.Logger;

public class TJobEntityFactory {

    protected static Logger transactionLogger = Loggers.transactionLogger;

    /**
     * 获取通用读锁实体 默认不能读取到
     *
     * @return
     */
    public static TJobEnityImpl createReadRejectTEnity(String prex_, String transtion_name, String unionKey, RedisDaoImpl dbDao) {
        TJobEnityImpl readTEnity = createReadTEnity(prex_, transtion_name, unionKey, dbDao);
        readTEnity.setRejectFlag(true);
        return readTEnity;
    }

    /**
     * 获取通用读锁实体 默认需要读取到
     * @return
     */
    public static TJobEnityImpl createReadTEnity(String prex_, String transtion_name, String unionKey, DBDao dbDao) {
        return new TJobEnityImpl(RedisKeyUtil.getCommonEntityKey(prex_, transtion_name, unionKey), transtion_name, dbDao);
    }

}
