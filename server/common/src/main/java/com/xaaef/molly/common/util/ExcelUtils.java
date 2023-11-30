package com.xaaef.molly.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
public class ExcelUtils {

    /**
     * 默认分页数据
     */
    private static final int PAGE_SIZE = 10000;

    /**
     * 最大分页数据
     */
    private static final int MAX_PAGE_SIZE = 60000;


    /**
     * 泛型 分页导出 为 Excel
     *
     * @param fileName 文件名称
     * @param pageSize 每页多少条数据，Excel每个Sheet最大极限6w条数据
     * @param dataList 数据列表
     * @return ByteArrayOutputStream
     */
    public static <T> ByteArrayOutputStream genPageExport(Integer pageSize, List<T> dataList) {
        if (pageSize == null || pageSize < 1) {
            pageSize = PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        int finalPageSize = pageSize;
        //创建一个Excel文件
        return getWorkbook((workbook) -> {
            if (!dataList.isEmpty()) {
                // 获取设备的总数量
                int total = dataList.size();
                if (total > finalPageSize) {
                    var pageNum = new AtomicInteger(1);
                    toPageIndexRange(total, finalPageSize, (fromIndex, toIndex) -> {
                        var rangeData = dataList.subList(fromIndex, toIndex);
                        genPageExport(workbook, String.format("第 %d 页", pageNum.get()), rangeData);
                        pageNum.getAndIncrement();
                    });
                } else {
                    genPageExport(workbook, String.format("第 %d 页", 1), dataList);
                }
            }
        });
    }


    /**
     * 泛型 分页导出 为 Excel 。每页默认 1w 条数据
     *
     * @param fileName 文件名称
     * @param dataList 数据列表
     * @return ByteArrayOutputStream
     */
    public static <T> ByteArrayOutputStream genPageExport(List<T> dataList) {
        return genPageExport(PAGE_SIZE, dataList);
    }


    /**
     * 泛型 分页导出 为 Excel 。每页默认 1w 条数据
     *
     * @param fileName 文件名称
     * @param dataList 数据列表
     * @return ResponseEntity<ByteArrayResource>
     */
    public static <T> ResponseEntity<ByteArrayResource> genPageExport(String fileName, List<T> dataList) {
        return genPageExport(fileName, PAGE_SIZE, dataList);
    }


    /**
     * 泛型 分页导出 为 Excel
     *
     * @param fileName 文件名称
     * @param pageSize 每页多少条数据，Excel每个Sheet最大极限6w条数据
     * @param dataList 数据列表
     * @return ResponseEntity<ByteArrayResource>
     */
    public static <T> ResponseEntity<ByteArrayResource> genPageExport(String fileName, Integer pageSize, List<T> dataList) {
        if (StrUtil.isEmpty(fileName)) {
            fileName = RandomUtil.randomString(12);
        } else {
            if (fileName.endsWith(".xlsx")) {
                fileName = fileName.replaceFirst(".xlsx", "");
            }
        }
        var os = genPageExport(pageSize, dataList);
        var headerValue = String.format("attachment;filename=%s.xlsx", fileNameEncode(fileName));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(new ByteArrayResource(os.toByteArray()));
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
     * 泛型 分页导出
     */
    private static <T> void genPageExport(HSSFWorkbook workbook, String sheetName, List<T> rangeData) {
        if (rangeData.isEmpty()) {
            return;
        }
        // 反射获取对象属性，和描述名称
        var schemaDescriptions = getFieldSchemaDescription(CollectionUtil.getFirst(rangeData));
        //创建工作表，指定工作表名称
        var sheet1 = workbook.createSheet(sheetName);
        sheet1.setDefaultColumnWidth(20);
        var titleRow = sheet1.createRow(0);
        // 设置标题
        setTitleStyle(titleRow, createTitleStyle(workbook), schemaDescriptions);
        var cellNum = schemaDescriptions.size();
        for (int i = 0; i < rangeData.size(); i++) {
            var data = rangeData.get(i);
            var row = sheet1.createRow((i + 1));
            var fieldsValue = ReflectUtil.getFieldsValue(data);
            for (int v = 0; v < cellNum; v++) {
                var cell = row.createCell(v);
                var val = fieldsValue[v];
                setCellValue(cell, val);
            }
        }
    }


    /**
     * 根据 对象类型 格式化之后设置到单元格内
     */
    private static void setCellValue(HSSFCell cell, Object obj) {
        if (obj == null) {
            cell.setCellType(CellType.BLANK);
            return;
        }
        if (obj instanceof Number) {
            cell.setCellType(CellType.NUMERIC);
            if (obj instanceof Long o2) {
                cell.setCellValue(o2);
            } else if (obj instanceof Integer o2) {
                cell.setCellValue(o2);
            } else if (obj instanceof Short o2) {
                cell.setCellValue(o2);
            } else if (obj instanceof Float o2) {
                cell.setCellValue(o2);
            } else if (obj instanceof Double o2) {
                cell.setCellValue(o2);
            } else if (obj instanceof Byte o2) {
                cell.setCellValue(o2);
            }
        } else {
            cell.setCellType(CellType.STRING);
            if (obj instanceof String o1) {
                cell.setCellValue(o1);
            } else if (obj instanceof Boolean o1) {
                cell.setCellType(CellType.BOOLEAN);
                cell.setCellValue(o1);
            } else if (obj instanceof Date o1) {
                cell.setCellValue(DateUtil.formatDateTime(o1));
            } else if (obj instanceof LocalDate o1) {
                cell.setCellValue(o1.format(DatePattern.NORM_DATE_FORMATTER));
            } else if (obj instanceof LocalTime o1) {
                cell.setCellValue(o1.format(DatePattern.NORM_TIME_FORMATTER));
            } else if (obj instanceof LocalDateTime o1) {
                cell.setCellValue(o1.format(DatePattern.NORM_DATETIME_FORMATTER));
            } else if (obj instanceof ZonedDateTime o1) {
                cell.setCellValue(o1.format(DatePattern.NORM_DATETIME_FORMATTER));
            } else if (obj instanceof Iterable<?> || obj.getClass().isArray() || obj instanceof Map<?, ?> || obj instanceof Enum<?>) {
                cell.setCellValue(JsonUtils.toFormatJson(obj));
            } else {
                cell.setCellValue(obj.toString());
            }
        }
    }


    /**
     * 反射获取 对象属性 @Schema注解上的description
     *
     * @return List<String>
     */
    private static List<String> getFieldSchemaDescription(Object obj) {
        return Arrays.stream(ReflectUtil.getFields(obj.getClass()))
                .map(field -> {
                    var ann = field.getAnnotation(Schema.class);
                    return ann == null ? field.getName() : ann.description();
                })
                .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * 获取一个 Workbook
     *
     * @return ResponseEntity<ByteArrayResource>
     */
    public static ByteArrayOutputStream getWorkbook(Consumer<HSSFWorkbook> workbookConsumer) {
        //在内存中创建一个Excel文件
        var workbook = new HSSFWorkbook();
        // 创建一个 消费者
        workbookConsumer.accept(workbook);
        var os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
            workbook.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            try {
                os.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
            } catch (IOException ex) {
                log.error(e.getMessage());
            }
        }
        return os;
    }


    /**
     * 文件名编码
     */
    private static String fileNameEncode(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }


    /**
     * 创建 标题 样式
     */
    private static HSSFCellStyle createTitleStyle(HSSFWorkbook workbook) {
        // 标题，颜色
        var titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        var font1 = workbook.createFont();
        font1.setBold(true); //加粗
        font1.setColor(Font.COLOR_NORMAL); //字体颜色
        titleStyle.setFont(font1);
        return titleStyle;
    }


    /**
     * 设置 标题 样式
     */
    private static void setTitleStyle(HSSFRow titleRow, HSSFCellStyle titleStyle, List<String> titleName) {
        for (int index = 0; index < titleName.size(); index++) {
            var cell = titleRow.createCell(index);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(titleName.get(index));
            cell.setCellType(CellType.STRING);
        }
    }


}
