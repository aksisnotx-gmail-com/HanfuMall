package com.app.domain.user.service;

import cn.hutool.core.bean.BeanUtil;
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
import com.app.domain.wallet.service.WalletService;
import com.sdk.exception.GlobalException;
import com.sdk.resp.RespEntity;
import com.sdk.util.asserts.AssertUtils;
import com.sdk.util.jwt.JWTUtils;
import com.sdk.util.md5.MD5Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 微信服务
 *
 * @author xxl
 * @since 2024/3/16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends AbstractService<UserMapper,UserEntity> {

    @Value("${wechat.id}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    private final WalletService walletService;

    private static final Integer WECHAT_LOGIN = 1;

    private static final Integer UN_WECHAT_LOGIN = 0;

    // 微信提供的API接口URL，需要替换为实际值
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public UserEntity loginWithWechat(WeChatLoginParam param) {
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
                //如果手机号不存在则注册
                UserEntity user = getUserByPhoneNumber(param.getPhoneNumber());
                if (Objects.isNull(user)) {
                    //创建用户
                    UserEntity entity = new UserEntity();
                    BeanUtil.copyProperties(param, entity);
                    entity.setId(param.getPhoneNumber());
                    entity.setPwd(param.getPhoneNumber());
                    //注册
                    register(entity,true);
                    //登录
                    return login(entity.getPhoneNumber(), entity.getPwd(),true);
                }else {
                    //登录
                    return login(user.getPhoneNumber(), user.getPwd(),true);
                }
            }
            throw new GlobalException("请求微信授权接口失败");
        } catch (HttpException | GlobalException e) {
            throw new GlobalException(e.getMessage());
        }
    }


    public UserEntity login(String phoneNumber, String password,Boolean isWeChatLogin) {
        UserEntity user = getUserByPhoneNumber(phoneNumber);
        AssertUtils.notNull(user, "用户不存在");
        AssertUtils.assertTrue(user.getIsWechatLogin().equals(UN_WECHAT_LOGIN), "前台用户请去注册");
        //isWeChatLogin 微信登录则用手机号码解密
        AssertUtils.assertTrue(MD5Utils.decrypt(isWeChatLogin ? phoneNumber : password,user.getPwd()), "密码错误");
        return LoginUser.store(user);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean register(UserEntity param,boolean isWeChatLogin) {
        UserEntity user = getUserByPhoneNumber(param.getPhoneNumber());
        AssertUtils.isNull(user, "用户已经存在");
        param.setRole(isWeChatLogin ? Role.BUYER : Role.ADMIN);
        param.setIsWechatLogin(isWeChatLogin ? WECHAT_LOGIN : UN_WECHAT_LOGIN);
        param.setPwd(MD5Utils.encrypt(param.getPwd()));
        //保存用户
        boolean save = this.save(param);
        //前台角色初始化钱包
        if (isWeChatLogin && save) {
            walletService.initWallet(param.getId());
        }
        return save;
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
