package com.xaaef.molly.system.controller;

import cn.hutool.core.io.FileUtil;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import cn.xuyanwu.spring.file.storage.UploadPretreatment;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.system.po.ImageSizePO;
import com.xaaef.molly.system.vo.FileInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/1/6 16:28
 */


@Slf4j
@Tag(name = "[ 通用 ] 文件上传")
@RestController
@AllArgsConstructor
@RequestMapping("/upload")
public class FileUploadController {

    private final FileStorageService storageService;


    /**
     * 上传文件，成功返回文件 url
     */
    @Operation(summary = "图片上传", description = "图片上传")
    @PostMapping("/image")
    public JsonResult<FileInfoVO> image(MultipartFile file, ImageSizePO size) {
        try {
            isImage(file.getOriginalFilename());
            var contentType = getContentType(file.getOriginalFilename());
            var now = LocalDate.now();
            var up = storageService.of(file)
                    .setObjectType("image")
                    .setContentType(contentType)
                    .setPath(
                            String.format("%d/%d/%d/", now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                    );
            if (size.getHeight() != null && size.getWidth() != null) {
                up.image(size.getWidth(), size.getHeight());
            }
            var fileInfo = up.upload();
            return JsonResult.success(
                    new FileInfoVO()
                            .setUrl(fileInfo.getUrl())
                            .setFilename(fileInfo.getFilename())
                            .setExt(fileInfo.getExt())
            );
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage(), FileInfoVO.class);
        }
    }


    /**
     * 头像上传，成功返回文件 url，会压缩
     */
    @Operation(summary = "头像上传", description = "头像上传 压缩头像的宽高 200 * 200")
    @PostMapping("/avatar")
    public JsonResult<FileInfoVO> avatar(MultipartFile file) {
        try {
            isImage(file.getOriginalFilename());
            var contentType = getContentType(file.getOriginalFilename());
            var now = LocalDate.now();
            var fileInfo = storageService.of(file)
                    .setObjectType("avatar")
                    .setContentType(contentType)
                    .image()  // 将图片大小调整到 200 * 200
                    .setPath(
                            String.format("%d/%d/%d/", now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                    )
                    .upload();
            return JsonResult.success(
                    new FileInfoVO()
                            .setUrl(fileInfo.getUrl())
                            .setFilename(fileInfo.getFilename())
                            .setExt(fileInfo.getExt())
            );
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage(), FileInfoVO.class);
        }
    }


    /**
     * 上传固件
     *
     * @param fileUrl
     * @author Wang Chen Chen
     * @date 2021/7/19 18:28
     */
    @Operation(summary = "文件删除", description = "文件删除")
    @DeleteMapping
    public JsonResult<Boolean> delete(String fileUrl) {
        var ok = storageService.delete(fileUrl);
        return JsonResult.success(ok);
    }


    private static final String IMAGE_REG = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG|.bmp|.BMP|.webp|.WEBP)$";

    /**
     * 判断是否是图片
     *
     * @param imageName 图片名称
     * @return boolean true,通过，false，没通过
     */
    public static void isImage(String imageName) {
        if (StringUtils.isBlank(imageName)) {
            throw new RuntimeException("图片名称必须填写！");
        }
        if (!imageName.matches(IMAGE_REG)) {
            throw new RuntimeException("只可以上传[ jpg , jpeg , png , bmp , webp ]其中的一种");
        }
    }


    /**
     * 根据文件后缀名，判断 ContentType
     *
     * @author WangChenChen
     * @date 2023/1/6 16:50
     */
    public static String getContentType(String fileName) {
        return switch (FileUtil.extName(fileName)) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "png" -> "image/png";
            case "ico" -> "image/x-icon";
            case "bmp" -> "image/bmp";
            case "wbmp" -> "image/vnd.wap.wbmp";
            case "webp" -> "image/webp";
            case "svg" -> "image/svg+xml";
            case "xml" -> "application/xml";
            case "json" -> "application/json";
            case "pdf" -> "application/pdf";
            case "doc", "docx" -> "application/msword";
            case "ppt", "pptx" -> "application/vnd.ms-powerpoint";
            case "xls", "xlsx" -> "application/vnd.ms-excel";
            case "html" -> "text/html";
            case "txt" -> "text/plain";
            case "yaml" -> "text/vnd.yaml";
            case "yml" -> "text/vnd.yml";
            case "mp3" -> "audio/mp3";
            case "mp4" -> "video/mp4";
            case "avi" -> "video/avi";
            case "mpg" -> "video/mpg";
            default -> "application/octet-stream";
        };
    }

}
