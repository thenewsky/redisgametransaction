package com.redis.transaction.factory;

import com.redis.log.Loggers;
import com.redis.transaction.db.RedisDao;
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
    public static TJobEnityImpl createReadRejectTEnity(RedisDao dbDao, String prex_, String entity_name) {
        TJobEnityImpl readTEnity = createReadTEnity(dbDao, prex_, entity_name);
        readTEnity.setRejectFlag(true);
        return readTEnity;
    }

    /**
     * 获取通用读锁实体 默认需要读取到
     *
     * @return
     */
    public static TJobEnityImpl createReadTEnity(RedisDao dbDao, String prex_, String entity_name) {
        return new TJobEnityImpl(dbDao, prex_, entity_name);
    }

}
