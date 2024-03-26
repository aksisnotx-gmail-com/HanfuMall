package com.app.domain.discovery.entity.vo;

import com.app.domain.discovery.entity.DiscoveryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @since 2024/3/26
 */
@Data
@Schema(description = "消息列表")
public class MessageListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1019293446124837957L;

    private List<DiscoveryEntity> unReadLikes;

    private Map<String,Object> unReadComments;

    public static MessageListVO create(List<DiscoveryEntity> unReadLikes, Map<String,Object> unReadComments){
        MessageListVO messageListVO = new MessageListVO();
        messageListVO.setUnReadLikes(unReadLikes);
        messageListVO.setUnReadComments(unReadComments);
        return messageListVO;
    }
}
