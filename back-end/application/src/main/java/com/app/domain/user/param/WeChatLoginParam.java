package com.app.domain.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xxl
 * @since 2024/3/16
 */
@Data
@Schema(name = "微信登录参数")
public class WeChatLoginParam {

    @Schema(description = "票据")
    private String code;

    @Schema(description = "加密数据")
    private String encryptedData;

    @Schema(description = "加密向量")
    private String iv;

    @Schema(description = "手机号")
    private String phoneNumber;

}
