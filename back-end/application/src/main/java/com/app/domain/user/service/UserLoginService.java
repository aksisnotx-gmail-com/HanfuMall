package com.app.domain.user.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.app.domain.user.param.WeChatLoginParam;
import com.app.domain.user.entity.UserEntity;
import com.sdk.exception.GlobalException;
import com.sdk.resp.RespEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信服务
 *
 * @author xxl
 * @since 2024/3/16
 */
@Service
@Slf4j
public class UserLoginService {

    // 微信提供的API接口URL，需要替换为实际值
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public RespEntity<UserEntity> loginWithWechat(WeChatLoginParam param) {
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WECHAT_LOGIN_URL, param.getAppId(), param.getSecret(), param.getCode());

        // 使用Hutool发送HTTP GET请求
        HttpResponse response = HttpRequest.get(url).execute();

        if (response.isOk()) {
            // 解析响应内容为JSON对象
            JSONObject result = JSONUtil.parseObj(response.body());
            // 检查是否存在错误码
            if (result.containsKey("errcode")) {
                // 处理错误情况，例如打印日志、抛出异常等
                throw new GlobalException("微信授权失败: " + result);
            }
            log.error(result.toString());
            return null;
        } else {
            // 处理请求失败情况
            throw new GlobalException("请求微信授权接口失败");
        }
    }
}
