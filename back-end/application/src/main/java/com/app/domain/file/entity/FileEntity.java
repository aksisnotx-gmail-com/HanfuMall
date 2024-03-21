package com.app.domain.file.entity;

import java.io.Serial;
import java.util.Date;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文件(SysFile)表实体类
 *
 * @author makejava
 * @since 2024-03-19 15:58:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_file")
@Schema(description = "文件实体类")
public class FileEntity extends Entity {

    @Serial
    private static final long serialVersionUID = -8568081916000156577L;

    //保存路径
    @Schema(description = "保存路径")
    private String path;

    //下载路径
    @Schema(description = "下载路径")
    private String url;
}

