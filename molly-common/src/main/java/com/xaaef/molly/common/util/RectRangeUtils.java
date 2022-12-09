package com.xaaef.molly.common.util;

import com.xaaef.molly.common.domain.RectPoint;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 矩形 范围 检测
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/20 9:11
 */

public class RectRangeUtils {

    /**
     * 在范围内的点位ID
     *
     * @author WangChenChen
     * @date 2022/5/11 16:10
     */
    public static <T> Set<T> isIn(List<RectPoint<T>> list, RectPoint<T> topLeft, RectPoint<T> bottomRight) {
        return list.isEmpty() ? Set.of() : list.stream()
                .filter(target -> isIn(target, topLeft, bottomRight))
                .map(RectPoint::getId)
                .collect(Collectors.toSet());
    }


    /**
     * $-------------------------
     * -                        -
     * -           +            -
     * -                        -
     * -                        -
     * -------------------------@
     * <p>
     *
     * @param target      (+)目标点位
     * @param topLeft     ($) 左上角
     * @param bottomRight (@) 右下角
     * @return true 在矩形内，false 没有在矩形内
     */
    public static <T> boolean isIn(RectPoint<T> target, RectPoint<T> topLeft, RectPoint<T> bottomRight) {
        // 如果在纬度的范围内
        if (isInRange(target.getX(), topLeft.getX(), bottomRight.getX())) {
            if (topLeft.getY() * bottomRight.getY() > 0) {
                return isInRange(target.getY(), topLeft.getY(), bottomRight.getY());
            } else {
                if (Math.abs(topLeft.getY()) + Math.abs(bottomRight.getY()) < 180) {
                    return isInRange(target.getY(), topLeft.getY(), bottomRight.getY());
                } else {
                    double left = Math.max(topLeft.getY(), bottomRight.getY());
                    double right = Math.min(topLeft.getY(), bottomRight.getY());
                    return isInRange(target.getY(), left, 180) || isInRange(target.getY(), right, -180);
                }
            }
        } else {
            return false;
        }
    }


    /**
     * 判断是否在经纬度范围内
     *
     * @param point
     * @param left
     * @param right
     * @return boolean
     */
    public static boolean isInRange(double point, double left, double right) {
        return point >= Math.min(left, right) && point <= Math.max(left, right);
    }


}
