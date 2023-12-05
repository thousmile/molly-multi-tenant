package com.xaaef.molly.common.util;

import cn.hutool.core.date.DateUtil;
import com.xaaef.molly.common.domain.ValueRange;
import com.xaaef.molly.common.enums.QueryTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * <p>
 * 日期时间工具类
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/2/4 10:57
 */


public class DateUtils {


    /**
     * 获取指定时间范围
     *
     * @author WangChenChen
     * @version 2.0
     * @date 2023/12/5 18:42
     */
    public static ValueRange<LocalDateTime> getDateTimeRange(QueryTypeEnum queryType, LocalDate startDate) {
        var now = LocalDateTime.now();
        var start = Date.from(LocalDateTime.of(startDate, now.toLocalTime()).atZone(ZoneId.systemDefault()).toInstant());
        switch (queryType) {
            case YEAR:
                var flag1 = startDate.getYear() == now.getYear();
                var yearEnd = flag1 ? now : LocalDateTime.of(startDate.with(TemporalAdjusters.lastDayOfYear()), LocalTime.MAX);
                var yearStart = DateUtil.beginOfYear(start).toLocalDateTime();
                return new ValueRange<>(yearStart, yearEnd);
            case QUARTER:
                var flag2 = startDate.getYear() == now.getYear() && startDate.getMonth() == now.getMonth();
                var quarterEnd = flag2 ? now : DateUtil.endOfQuarter(start).toLocalDateTime();
                var quarterStart = DateUtil.beginOfQuarter(start).toLocalDateTime();
                return new ValueRange<>(quarterStart, quarterEnd);
            case MONTH:
                var flag3 = startDate.getYear() == now.getYear() && startDate.getMonth() == now.getMonth();
                var monthEnd = flag3 ? now : DateUtil.endOfMonth(start).toLocalDateTime();
                var monthStart = DateUtil.beginOfMonth(start).toLocalDateTime();
                return new ValueRange<>(monthStart, monthEnd);
            case WEEK:
                var weekEnd = startDate.equals(now.toLocalDate()) ? now : DateUtil.endOfWeek(start).toLocalDateTime();
                var weekStart = DateUtil.beginOfWeek(start).toLocalDateTime();
                return new ValueRange<>(weekStart, weekEnd);
            default:
                var dayEnd = startDate.equals(now.toLocalDate()) ? now.toLocalTime() : LocalTime.MAX;
                return new ValueRange<>(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(startDate, dayEnd));
        }
    }


    /**
     * 根据 类型，获取之前的日期
     * <p>
     * 查询类型！年: year , 季度: quarter , 月: month , 周: week, 日: date
     *
     * @author WangChenChen
     * @date 2022/8/19 16:05
     */
    public static LocalDate minusQueryType(QueryTypeEnum queryType, LocalDate date, int step) {
        return switch (queryType) {
            case YEAR -> date.minusYears(step);
            case QUARTER -> date.minusYears(3L * step);
            case MONTH -> date.minusMonths(step);
            case WEEK -> date.minusWeeks(step);
            default -> date.minusDays(step);
        };
    }


}
