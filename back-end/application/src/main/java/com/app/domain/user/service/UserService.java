package com.app.domain.user.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.user.entity.LoginUser;
import com.app.domain.user.enums.Role;
import com.app.domain.user.mapper.UserMapper;
import com.app.domain.user.param.WeChatLoginParam;
import com.app.domain.user.entity.UserEntity;
import com.sdk.exception.GlobalException;
import com.sdk.resp.RespEntity;
import com.sdk.util.asserts.AssertUtils;
import com.sdk.util.jwt.JWTUtils;
import com.sdk.util.md5.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 微信服务
 *
 * @author xxl
 * @since 2024/3/16
 */
@Service
@Slf4j
public class UserService extends AbstractService<UserMapper,UserEntity> {

    @Value("${wechat.id}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    public static final Integer WECHAT_LOGIN = 1;

    public static final Integer UN_WECHAT_LOGIN = 0;

    // 微信提供的API接口URL，需要替换为实际值
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

   /* public UserEntity loginWithWechat(WeChatLoginParam param) {
        final String resUrl = "%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        String url = String.format(resUrl, WECHAT_LOGIN_URL, appid, secret, param.getCode());
        // 使用HuTool发送HTTP GET请求
        try(HttpResponse response = HttpRequest.get(url).execute()) {
            if (response.isOk()) {
                // 解析响应内容为JSON对象
                JSONObject result = JSONUtil.parseObj(response.body());
                // 检查是否存在错误码
                if (result.containsKey("errcode")) {
                    // 处理错误情况，例如打印日志、抛出异常等
                    throw new GlobalException("微信授权失败: " + result);
                }

                String sessionKey = (String) result.get("session_key");
                //用session_key解密
                String decrypt = decrypt(param.getEncryptedData(), sessionKey, param.getIv());
                UserEntity.WeChatUser weChatUser = JSONUtil.toBean(decrypt, UserEntity.WeChatUser.class);
                UserEntity user = new UserEntity();
                user.setId(weChatUser.getOpenId());
                user.setAvatar(weChatUser.getAvatarUrl());
                user.setGender(weChatUser.getGender());
                user.setPhoneNumber(param.getPhoneNumber());
                user.setNickname(weChatUser.getNickName());

                //如果ID不存在则注册
                if (Objects.isNull(getById(user.getId(),false))) {
                    return register();
                }else {

                }

                //登录
                return login(user, true);
            }
            throw new GlobalException("请求微信授权接口失败");
        } catch (HttpException | GlobalException e) {
            throw new GlobalException(e.getMessage());
        }
    }*/


    public UserEntity login(String phoneNumber, String password) {
        UserEntity user = getUserByPhoneNumber(phoneNumber);
        AssertUtils.notNull(user, "用户不存在");
        AssertUtils.assertTrue(MD5Utils.encrypt(password).equals(user.getPwd()), "密码错误");
        return LoginUser.store(user);
    }

    public Boolean register(UserEntity param,boolean isWeChatLogin) {
        UserEntity user = getUserByPhoneNumber(param.getPhoneNumber());
        AssertUtils.isNull(user, "用户已经存在");
        param.setRole(isWeChatLogin ? Role.ADMIN : Role.BUYER);
        param.setIsWechatLogin(isWeChatLogin ? WECHAT_LOGIN : UN_WECHAT_LOGIN);
        param.setPwd(MD5Utils.encrypt(param.getPwd()));
        //保存用户
        return this.save(param);
    }

    public UserEntity modifyUserInfo(UserEntity param) {
        UserEntity user = getById(param.getId());
        user.setNickname(param.getNickname());
        user.setAvatar(param.getAvatar());
        user.setCoordinate(param.getCoordinate());
        user.setShippingAddress(param.getShippingAddress());
        user.setEmail(param.getEmail());
        user.setGender(param.getGender());
        //更新用户
        boolean update = this.updateById(param);
        if (update) {
            UserEntity entity = this.getById(param.getId());
            entity.setToken(LoginUser.getToken());
            return LoginUser.update(entity);
        }
        throw new GlobalException("更新用户信息失败");
    }

    private UserEntity getUserByPhoneNumber(String phoneNumber) {
        return this.lambdaQuery().eq(UserEntity::getPhoneNumber, phoneNumber).one();
    }

    private String decrypt(String encryptedData, String sessionKey, String iv) {
        // 使用Base64解码
        byte[] dataByte = Base64.decode(encryptedData);
        byte[] keyByte = Base64.decode(sessionKey);
        byte[] ivByte = Base64.decode(iv);

        // 创建AES实例并设置模式和填充方式
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keyByte, ivByte);

        // 解密
        byte[] resultByte = aes.decrypt(dataByte);

        // 转换解密结果为字符串
        return new String(resultByte, CharsetUtil.CHARSET_UTF_8);
    }


}
