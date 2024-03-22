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
}
