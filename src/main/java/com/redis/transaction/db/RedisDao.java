package com.redis.transaction.db;

import com.redis.log.Loggers;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by zhujin on 16/12/19.
 * blog:www.zhujin.me
 * email: zhujin.nova@gmail.com
 */
public interface RedisDao {
    public void expire(String key, int seconds);
    public boolean deleteKey(String key);
    public boolean setNxString(String lockName, String lockContent, int seconds) throws Exception;
    public boolean setHnxString(String key, String field, String value) throws Exception;
    public String getString(String key);
    public String getString(String key, int seconds);
    public void setString(String key, String object);
    public void setString(String key, String value, int seconds);
    public boolean exists(String key);
}
