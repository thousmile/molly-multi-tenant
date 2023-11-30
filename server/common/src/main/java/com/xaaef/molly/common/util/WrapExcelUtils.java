package com.xaaef.molly.common.util;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


/**
 * <p>
 * Excel 工具类
 * </p>
 *
 * @author WangChenChen
 * @version 1.2
 * @date 2023/11/23 14:50
 */

@Slf4j
public class WrapExcelUtils {

    /**
     * 默认分页数据
     */
    private static final int PAGE_SIZE = 10000;

    /**
     * 最大分页数据
     */
    private static final int MAX_PAGE_SIZE = 60000;

    /**
     * 泛型 分页导出 为 Excel 。每页默认 1w 条数据
     *
     * @param fileName 文件名称
     * @param dataList 数据列表
     * @return ByteArrayOutputStream
     */
    public static <T> ByteArrayOutputStream genPageWrite(List<T> dataList) {
        return genPageWrite(PAGE_SIZE, dataList);
    }


    /**
     * 泛型 分页导出 为 Excel 。每页默认 1w 条数据
     *
     * @param fileName 文件名称
     * @param dataList 数据列表
     * @return ResponseEntity<ByteArrayResource>
     */
    public static <T> ResponseEntity<ByteArrayResource> genPageWrite(String fileName, List<T> dataList) {
        return genPageWrite(fileName, PAGE_SIZE, dataList);
    }


    /**
     * 泛型 分页导出 为 Excel
     *
     * @param fileName 文件名称
     * @param pageSize 每页多少条数据，Excel每个Sheet最大极限6w条数据
     * @param dataList 数据列表
     * @return ResponseEntity<ByteArrayResource>
     */
    public static <T> ResponseEntity<ByteArrayResource> genPageWrite(String fileName, Integer pageSize, List<T> dataList) {
        if (StrUtil.isEmpty(fileName)) {
            fileName = RandomUtil.randomString(12);
        } else {
            if (fileName.endsWith(".xlsx")) {
                fileName = fileName.replaceFirst(".xlsx", "");
            }
        }
        var os = genPageWrite(pageSize, dataList);
        var headerValue = String.format("attachment;filename=%s.xlsx", fileNameEncode(fileName));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(new ByteArrayResource(os.toByteArray()));
    }


    /**
     * 泛型 分页导出 为 Excel
     *
     * @param fileName 文件名称
     * @param pageSize 每页多少条数据，Excel每个Sheet最大极限6w条数据
     * @param dataList 数据列表
     * @return ByteArrayOutputStream
     */
    public static <T> ByteArrayOutputStream genPageWrite(Integer pageSize, List<T> dataList) {
        if (pageSize == null || pageSize < 1) {
            pageSize = PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        var bigWriter = ExcelUtil.getBigWriter();
        if (!dataList.isEmpty()) {
            // 获取对象字段的别名
            Map<String, String> fieldSchemaDescription = getFieldSchemaDescription(dataList.getFirst());
            bigWriter.setOnlyAlias(true).setHeaderAlias(fieldSchemaDescription);
            // 获取设备的总数量
            int total = dataList.size();
            if (total > pageSize) {
                // 删除 第一个 Sheet
                bigWriter.getWorkbook().removeSheetAt(0);
                var pageNum = new AtomicInteger(1);
                // 分页写入
                toPageIndexRange(total, pageSize, (fromIndex, toIndex) -> {
                    var rangeData = dataList.subList(fromIndex, toIndex);
                    var sheetName = String.format("第 %d 页", pageNum.get());
                    bigWriter.setSheet(sheetName).write(rangeData);
                    pageNum.getAndIncrement();
                });
            } else {
                bigWriter.write(dataList);
            }
        }
        var result = new ByteArrayOutputStream();
        bigWriter.flush(result, true);
        return result;
    }


    /**
     * 计算分页范围
     * 根据总数和分页数量，计算每个分页的 开始索引 和 结束索引
     * 假设：总共300条数据。每页100条。
     * 列如：第一页：0~99 、第二页：100~199 、第三页：200~299 以此类推
     */
    private static void toPageIndexRange(int total, int pageSize, BiConsumer<Integer, Integer> consumer) {
        // 总页数
        int totalPage = PageUtil.totalPage(total, pageSize);
        // 起始索引
        var startIndex = new AtomicInteger(0);
        for (int i = 0; i < totalPage; i++) {
            int fromIndex = startIndex.get();
            var toIndex = (fromIndex + pageSize);
            startIndex.set(toIndex);
            if (toIndex >= total) {
                toIndex = total;
            }
            consumer.accept(fromIndex, toIndex);
        }
    }


    /**
     * 反射获取 对象属性 @Schema注解上的description
     * <p>
     * key : 属性名称
     * value :  cn.hutool.core.annotation.Alias.value
     * 或者 io.swagger.v3.oas.annotations.media.Schema.description
     *
     * @return Map<String, String>
     */
    private static Map<String, String> getFieldSchemaDescription(Object obj) {
        return Arrays.stream(ReflectUtil.getFields(obj.getClass()))
                .collect(Collectors.toMap(Field::getName, field -> {
                    var ann1 = field.getAnnotation(cn.hutool.core.annotation.Alias.class);
                    if (ann1 != null) {
                        return ann1.value();
                    }
                    var ann2 = field.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
                    if (ann2 != null) {
                        return ann2.description();
                    }
                    return field.getName();
                }, (k1, k2) -> k1, LinkedHashMap::new));
    }


    /**
     * 文件名编码
     */
    private static String fileNameEncode(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

}
