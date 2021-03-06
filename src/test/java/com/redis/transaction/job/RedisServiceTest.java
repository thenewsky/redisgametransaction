package com.redis.transaction.job;

import com.redis.transaction.db.DBConfigManager;
import com.redis.transaction.db.RedisDaoImpl;
import org.jdom.DataConversionException;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class RedisServiceTest {
    public static void main(String[] args) throws DataConversionException {
        RedisDaoImpl redisService = new RedisDaoImpl();
        redisService.setJedisPool(DBConfigManager.getJedisPool());

        String testKey = "ketest100";
        redisService.setString(testKey, "100");
        String number = redisService.getString(testKey);
        System.out.println(number);
    }
}
