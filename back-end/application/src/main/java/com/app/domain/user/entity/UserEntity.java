package com.app.domain.user.entity;

import com.app.domain.base.Entity;
import com.app.domain.user.enums.Role;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author xxl
 * @since 2024/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class UserEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5927060186908035358L;

    //id
    private String id;

    //角色
    private Role role;

    //密码
    private String pwd;

    //昵称
    private String nickname;

    //头像url
    private String avatar;

    //坐标
    private String coordinate;

    //收货地址(可能是多个)
    private String shippingAddress;

    //手机号码
    private String phoneNumber;

    @TableField(exist = false)
    private String token;
}
