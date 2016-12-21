package com.redis.transaction.db;

import com.redis.config.GlobalConstants;
import com.redis.util.StringUtils;


/**
 * Created by zhujin on 16/12/19.
 * blog:www.zhujin.me
 * email: zhujin.nova@gmail.com
 */
public class RedisKeyUtil {

    /**
     * @param name
     * @param redisKey
     * @param uuid
     * @return
     */
//    public static String getCommonEntityKey(String redisKey, String name, String uuid) {
//        return redisKey + name + GlobalConstants.Strings.commonSplitString + uuid;
////    }


    /**
     * prex_#transtion_name#player_id#entity_key
     *
     * @param key_pre
     * @param transaction_name
     * @param entity_name
     * @return
     */
    public static String getLockKey(String key_pre, String transaction_name, String entity_name) {
        return key_pre + transaction_name + GlobalConstants.Strings.commonSplitString + entity_name;
    }


}
