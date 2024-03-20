package com.app.config;

import cn.hutool.core.util.StrUtil;
import com.app.domain.user.entity.LoginUser;
import com.app.toolkit.redis.RedisUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sdk.util.asserts.AssertUtils;
import com.sdk.util.thead.TheadUtils;
import com.xxl.sdk.log.AsyncLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

/**
 * mvc配置
 *
 * @author xxl
 * @since 2023/9/16
 */
@ConfigurationProperties(prefix = "auth-path")
@Configuration
@EnableAspectJAutoProxy
@Data
public class MvcConfiguration implements WebMvcConfigurer, HandlerInterceptor, MetaObjectHandler {

    private static final String PATH = "/**";

    private String[] exclude;

    private String tokenName;

    /**
     * 是否开启校验
     */
    private boolean enable;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!enable) {
            return true;
        }
        String token = request.getHeader(tokenName);
        token = StrUtil.isBlank(token) ? request.getHeader(tokenName.toLowerCase()) : token;
        AssertUtils.notNull(token, "TOKEN不存在请先登录");
        LoginUser.check(token);
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration authInterceptorRegistration = registry.addInterceptor(this);
        authInterceptorRegistration.addPathPatterns(PATH);
        authInterceptorRegistration.excludePathPatterns(exclude);
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了,这里设置2个小时
        config.setMaxAge(360000L);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public LoginUser setRedisUtils(RedisUtils redisUtils) {
        return new LoginUser(redisUtils);
    }

    @Bean
    public AsyncLogger logger() {
        return new AsyncLogger(TheadUtils.createThreadPool());
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime",new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        metaObject.setValue("updateTime",new Date());
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
