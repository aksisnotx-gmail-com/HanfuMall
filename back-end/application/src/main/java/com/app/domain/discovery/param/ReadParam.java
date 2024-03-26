package com.app.domain.discovery.param;

import com.app.domain.discovery.enums.ReadType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/26
 */
@Data
@Schema(description = "已读参数")
public class ReadParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 801708964614231575L;

    @Schema(description = "已读类型")
    @NotNull(message = "已读类型不能为空")
    private ReadType type;

    @Schema(description = "消息ID: 1.已读点赞ID就是发现ID 2.已读评论、已读回复ID就是评论ID")
    @NotEmpty(message = "消息ID不能为空")
    private List<String> ids;
}
