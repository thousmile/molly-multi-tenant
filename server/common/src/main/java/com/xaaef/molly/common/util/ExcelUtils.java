package com.xaaef.molly.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
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
     * 设备导出的 Excel
     *
     * @author WangChenChen
     * @date 2023/9/21 14:51
     */
    public static <T> ResponseEntity<ByteArrayResource> deviceExport(String fileName, List<T> dataList) {
        //创建一个Excel文件
        return getWorkbook(fileName, (workbook) -> {
            if (!dataList.isEmpty()) {
                // 获取设备的总数量
                int total = dataList.size();
                int pageSize = 10000;
                if (total > pageSize) {
                    int pages = (total / pageSize) + 1;
                    var startIndex = new AtomicInteger(0);
                    for (int i = 0; i < pages; i++) {
                        int fromIndex = startIndex.get();
                        var toIndex = (fromIndex + pageSize);
                        startIndex.set(toIndex);
                        if (toIndex >= total) {
                            toIndex = total;
                        }
                        var rangeData = dataList.subList(fromIndex, toIndex);
                        var pageNum = i + 1;
                        pageExport(workbook, String.format("第 %d 页", pageNum), rangeData);
                    }
                } else {
                    pageExport(workbook, String.format("第 %d 页", 1), dataList);
                }
            }
        });
    }


    /**
     * 分页导出
     *
     * @author WangChenChen
     * @version 2.0
     * @date 2023/11/23 12:32
     */
    private static <T> void pageExport(HSSFWorkbook workbook, String sheetName, List<T> rangeData) {
        // 反射获取对象属性，和描述名称
        var fieldNames = Arrays.stream(ReflectUtil.getFields(rangeData.getFirst().getClass()))
                .map(field -> {
                    var ann = field.getAnnotation(Schema.class);
                    return ann == null ? StrUtil.EMPTY : ann.description();
                })
                .collect(Collectors.toCollection(LinkedList::new));
        //创建工作表，指定工作表名称
        var sheet1 = workbook.createSheet(sheetName);
        sheet1.setDefaultColumnWidth(20);
        var titleRow = sheet1.createRow(0);
        // 设置标题
        setTitle(titleRow, createTitleStyle(workbook), fieldNames);

        var cellNum = fieldNames.size();
        for (int ind = 0; ind < rangeData.size(); ind++) {
            var a1 = rangeData.get(ind);
            var row1 = sheet1.createRow((ind + 1));
            var fieldsValue = ReflectUtil.getFieldsValue(a1);
            for (int v = 0; v < cellNum; v++) {
                var cell0 = row1.createCell(v);
                cell0.setCellType(CellType.STRING);
                var obj = fieldsValue[v];
                if (obj == null) {
                    cell0.setCellValue(StrUtil.EMPTY);
                    break;
                }
                if (obj instanceof Long o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Integer o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Short o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Float o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Double o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof String o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Byte o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Boolean o1) {
                    cell0.setCellValue(o1);
                } else if (obj instanceof Date o1) {
                    cell0.setCellValue(DateUtil.formatDateTime(o1));
                } else if (obj instanceof LocalDate o1) {
                    cell0.setCellValue(o1.format(DatePattern.NORM_DATE_FORMATTER));
                } else if (obj instanceof LocalTime o1) {
                    cell0.setCellValue(o1.format(DatePattern.NORM_TIME_FORMATTER));
                } else if (obj instanceof LocalDateTime o1) {
                    cell0.setCellValue(o1.format(DatePattern.NORM_DATETIME_FORMATTER));
                } else if (obj instanceof ZonedDateTime o1) {
                    cell0.setCellValue(o1.format(DatePattern.NORM_DATETIME_FORMATTER));
                } else if (obj instanceof Iterable<?> || obj instanceof Map<?, ?> || obj instanceof Enum<?> || obj.getClass().isArray()) {
                    cell0.setCellValue(JsonUtils.toFormatJson(obj));
                } else {
                    cell0.setCellValue(obj.toString());
                }
            }
        }
    }


    /**
     * 获取一个 Workbook
     *
     * @date 2023/11/23 11:20
     */
    public static ResponseEntity<ByteArrayResource> getWorkbook(String fileName, Consumer<HSSFWorkbook> workbookConsumer) {
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
        if (fileName.endsWith(".xlsx")) {
            fileName = fileName.replaceFirst(".xlsx", "");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", String.format("attachment;filename=%s.xlsx", fileNameEncode(fileName)))
                .body(new ByteArrayResource(os.toByteArray()));
    }


    /**
     * 文件名编码
     *
     * @date 2023/11/23 11:20
     */
    private static String fileNameEncode(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }


    /**
     * 创建标题样式
     *
     * @date 2023/11/23 11:20
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
     * 设置 标题
     *
     * @date 2023/11/23 11:20
     */
    private static void setTitle(HSSFRow titleRow, HSSFCellStyle titleStyle, List<String> titleName) {
        for (int index = 0; index < titleName.size(); index++) {
            var cell = titleRow.createCell(index);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(titleName.get(index));
            cell.setCellType(CellType.STRING);
        }
    }


}
