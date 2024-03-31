package com.app.domain.user.entity;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.app.domain.user.enums.Role;
import com.app.toolkit.redis.RedisUtils;
import com.sdk.exception.GlobalException;
import com.sdk.util.asserts.AssertUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.app.domain.user.enums.Role.ADMIN;

/**
 * @author xxl
 * @since 2024/3/16
 */
@Data
@Slf4j
public  class LoginUser {

    private static RedisUtils redisUtils;

    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    private static UserEntity entity;

    public static final long HALF_MONTH =  60 * 60 * 24 * 15L;

    public LoginUser(RedisUtils redisUtils) {
        LoginUser.redisUtils = redisUtils;
    }

    public static UserEntity getLoginUser() {
        String token = LOCAL.get();
        AssertUtils.notNull(token, "TOKEN异常请重新登录");
        //从redis中获取查看key是否过时
        AssertUtils.assertTrue(redisUtils.hasKey(token), "TOKEN失效，请重新登录");
        return redisUtils.get(token, UserEntity.class);
    }

    public static String getLoginUserId() {
        return  getLoginUser().getId();
    }

    public static Role getLoginUserRole() {
        return  getLoginUser().getRole();
    }

    public static String getToken() {
        return  getLoginUser().getToken();
    }

    public static void checkToken(String token){
        AssertUtils.assertTrue(redisUtils.hasKey(token), "TOKEN失效，请重新登录");
        LOCAL.set(token);
    }

    public static UserEntity store(UserEntity user) {
        final String token = createToken(user.getId());
        user.setToken(token);
        redisUtils.opsForValue(token,user,HALF_MONTH);
        //存到本地内存中
        LOCAL.set(token);
        return getLoginUser();
    }

    public static UserEntity update(UserEntity user) {
        //更新用户
        redisUtils.opsForValue(user.getToken(),user);
        return getLoginUser();
    }

    public static boolean remove(String userId) {
        return redisUtils.delete(createToken(userId)) == 1L;
    }

    private static String createToken(String userId) {
        //两次BASE64加密就是TOKEN
        return Base64.encode(Base64.encode(userId));
    }
}
