package com.app.domain.user.entity;

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

    private static final ThreadLocal<UserEntity> LOCAL = new ThreadLocal<>();

    private static UserEntity entity;

    public static final long HALF_MONTH =  60 * 60 * 24 * 15L;

    public LoginUser(RedisUtils redisUtils) {
        LoginUser.redisUtils = redisUtils;
    }

    public static UserEntity getLoginUser() {
        UserEntity user = null;
        try {
            user = LOCAL.get();
            AssertUtils.isNull(user, "token异常，请重新登录");
            return user;
        } catch (GlobalException e) {
            log.error(e.getMsg());
        }

        //暂时测试使用
        user = new UserEntity();
        user.setRole(ADMIN);
        user.setId(IdUtil.simpleUUID());
        return user;
    }

    public static String getLoginUserId() {
        return  getLoginUser().getId();
    }

    public static Role getLoginUserRole() {
        return  getLoginUser().getRole();
    }

    public static void check(String token) {
        AssertUtils.assertTrue(redisUtils.hasKey(token), "TOKEN异常/失效，请重新登录");
        UserEntity user = redisUtils.get(token, UserEntity.class);
        user.setToken(token);
        LOCAL.set(user);
    }

    public static UserEntity store(UserEntity user,long time) {
        final String token = UUID.randomUUID().toString();
        user.setToken(token);
        redisUtils.opsForValue(token,user,time);
        return redisUtils.get(token,UserEntity.class);
    }

    public static UserEntity store(UserEntity user) {
       return store(user,HALF_MONTH);
    }
}
