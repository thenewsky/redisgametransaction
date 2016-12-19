package com.redis.transaction.service;

import com.redis.util.FileUtil;
import com.redis.config.GlobalConstants;
import com.redis.util.JdomUtils;
import org.jdom.DataConversionException;
import org.jdom.Element;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by jiangwenping on 16/11/29.
 */
public class ConfigService {
    private static JedisPool jedisPool;
    static {
        try {
            initRedis(GlobalConstants.RedisConfigFile.REDIS_CONFIG, initRediPoolConfig(GlobalConstants.RedisConfigFile.REDIS_POOL_CONIFG));
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    private ConfigService() {
    }

    private static JedisPoolConfig initRediPoolConfig(String jedisPoolConfigUrl) throws DataConversionException {
        Element element = JdomUtils.getRootElemet(FileUtil.getConfigURL(jedisPoolConfigUrl).getFile());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        int maxIdle = element.getAttribute("maxIdle").getIntValue();
        boolean testWhileIdle = element.getAttribute("testWhileIdle").getBooleanValue();
        int timeBetweenEvictionRunsMillis = element.getAttribute("timeBetweenEvictionRunsMillis").getIntValue();
        int numTestsPerEvictionRun = element.getAttribute("numTestsPerEvictionRun").getIntValue();
        int minEvictableIdleTimeMillis = element.getAttribute("minEvictableIdleTimeMillis").getIntValue();
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return jedisPoolConfig;
    }


    public static void initRedis(String cfgUrl, JedisPoolConfig jedisPoolConfig) throws DataConversionException {
        Element element = JdomUtils.getRootElemet(FileUtil.getConfigURL(cfgUrl).getFile());
        String host = element.getAttribute("host").getValue();
        int port = element.getAttribute("port").getIntValue();
        int timeout = element.getAttribute("timeout").getIntValue();
        int database = element.getAttribute("database").getIntValue();
        boolean hasPassword = element.getAttribute("password") != null;
        if (hasPassword) {
            String password = element.getAttribute("password").getValue();
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        }
    }

}
