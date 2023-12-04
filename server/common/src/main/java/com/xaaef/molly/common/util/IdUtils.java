package com.xaaef.molly.common.util;

import cn.hutool.core.util.RandomUtil;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;


/**
 * <p>
 * Twitter 魔改的 雪花ID 算法
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/12 15:43
 */


public class IdUtils {

    static {
        // workerId 范围 1~63。
        var workerId = (short) RandomUtil.randomInt(1, 63);
        var options = new IdGeneratorOptions(workerId);
        YitIdHelper.setIdGenerator(options);
    }


    /**
     * [单机版] 雪花ID
     */
    public static long getStandaloneId() {
        return YitIdHelper.nextId();
    }


    /**
     * [单机版] 雪花ID
     */
    public static String getStandaloneStrId() {
        return String.valueOf(YitIdHelper.nextId());
    }

}
