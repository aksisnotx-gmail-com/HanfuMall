package com.app.domain.user.entity;

import com.app.domain.base.Entity;
import com.app.domain.user.enums.Role;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_user",autoResultMap = true)
@Schema(description = "用户实体")
public class UserEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5927060186908035358L;

    //角色
    @Schema(description = "角色")
    @JsonView({IGNORE.class})
    private Role role;

    //密码
    @Schema(description = "密码")
    @JsonView({INSERT.class, LOGIN.class})
    @NotBlank(message = "密码不能为空",groups = {INSERT.class, LOGIN.class})
    private String pwd;

    //昵称
    @Schema(description = "昵称")
    @JsonView({INSERT.class,UPDATE.class})
    @NotBlank(message = "昵称不能为空",groups = {INSERT.class,UPDATE.class})
    private String nickname;

    //头像url
    @Schema(description = "头像url")
    @JsonView({INSERT.class,UPDATE.class})
    private String avatar;

    //坐标
    @Schema(description = "坐标")
    @JsonView({IGNORE.class,UPDATE.class})
    private String coordinate;

    //收货地址(可能是多个)
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonView({IGNORE.class,UPDATE.class})
    private List<String> shippingAddress;

    //手机号码
    @Schema(description = "手机号码")
    @JsonView({INSERT.class, LOGIN.class})
    @NotBlank(message = "手机号码不能为空",groups = {INSERT.class, LOGIN.class})
    private String phoneNumber;

    @Schema(description = "邮箱")
    @JsonView({INSERT.class,UPDATE.class})
    @Email(message = "邮箱格式不正确",groups = {INSERT.class,UPDATE.class})
    private String email;

    //是否微信登录 1 是 0 否
    @Schema(description = "是否微信登录 1 是 0 否")
    @JsonView({IGNORE.class})
    private Integer isWechatLogin;

    @TableField(exist = false)
    @JsonView({IGNORE.class})
    private String token;

    //0 是男 1 是女
    @JsonView({INSERT.class,UPDATE.class})
    @Schema(description = "性别 0 是男 1 是女")
    @Min(value = 0,message = "性别 0 是男 1 是女",groups = {INSERT.class,UPDATE.class})
    @Max(value = 1,message = "性别 0 是男 1 是女",groups = {INSERT.class,UPDATE.class})
    public Integer gender;
}
