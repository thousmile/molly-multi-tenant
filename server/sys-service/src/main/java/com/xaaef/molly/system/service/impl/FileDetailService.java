package com.xaaef.molly.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.recorder.FileRecorder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.system.entity.SysFileDetail;
import com.xaaef.molly.system.mapper.SysFileDetailMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/1/6 17:12
 */


@Slf4j
@Service
@AllArgsConstructor
public class FileDetailService extends ServiceImpl<SysFileDetailMapper, SysFileDetail> implements FileRecorder {


    @Override
    public boolean record(FileInfo info) {
        var detail = BeanUtil.copyProperties(info, SysFileDetail.class, "attr");
        //这是手动获 取附加属性字典 并转成 json 字符串，方便存储在数据库中
        if (info.getAttr() != null) {
            detail.setAttr(JsonUtils.toJson(info.getAttr()));
        }
        var b = this.save(detail);
        if (b) {
            info.setId(detail.getId());
        }
        return b;
    }


    @Override
    public FileInfo getByUrl(String url) {
        var detail = getOne(
                new LambdaQueryWrapper<SysFileDetail>()
                        .eq(SysFileDetail::getUrl, url)
        );
        if (detail == null) {
            return null;
        }
        var info = BeanUtil.copyProperties(detail, FileInfo.class, "attr");
        //这是手动获取数据库中的 json 字符串 并转成 附加属性字典，方便使用
        if (StrUtil.isNotBlank(detail.getAttr())) {
            info.setAttr(JsonUtils.toPojo(detail.getAttr(), Dict.class));
        }
        return info;
    }


    @Override
    public boolean delete(String url) {
        return this.remove(
                new LambdaQueryWrapper<SysFileDetail>()
                        .eq(SysFileDetail::getUrl, url)
        );
    }


}
