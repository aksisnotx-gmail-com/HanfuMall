package com.app.domain.user.param;

import com.app.domain.base.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author xxl
 * @since 2024/3/16
 */
@Data
@Schema(name = "微信登录参数")
public class WeChatLoginParam {

    @Schema(description = "票据")
    @NotBlank(message = "票据不能为空")
    private String code;

    @Schema(description = "加密数据")
    @NotBlank(message = "加密数据不能为空")
    private String encryptedData;

    @Schema(description = "加密向量")
    @NotBlank(message = "加密向量不能为空")
    private String iv;

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号格式不正确")
    private String phoneNumber;

    @Schema(description = "号码")
    @NotBlank(message = "号码不能为空")
    private String nickname;

    @Schema(description = "头像")
    @NotBlank(message = "头像不能为空")
    private String avatar;

    @Schema(description = "性别 0 是男 1 是女")
    @Min(value = 0,message = "性别 0 是男 1 是女")
    @Max(value = 1,message = "性别 0 是男 1 是女")
    private Integer gender;

}
