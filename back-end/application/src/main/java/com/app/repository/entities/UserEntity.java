package com.app.repository.entities;

import com.app.repository.entities.common.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author xxl
 * @since 2024/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5927060186908035358L;

    @TableField(exist = false)
    private String token;
}
