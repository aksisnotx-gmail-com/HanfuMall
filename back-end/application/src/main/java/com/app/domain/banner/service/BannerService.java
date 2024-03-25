package com.app.domain.banner.service;

import com.app.domain.banner.entity.BannerEntity;
import com.app.domain.banner.mapper.BannerMapper;
import com.app.domain.base.AbstractService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app.domain.banner.service.BannerService.BANNER;

/**
 * @author xxl
 * @since 2024/3/23
 */
@Service
public class BannerService extends AbstractService<BannerMapper, BannerEntity> {

    public static final String BANNER = "BANNER";

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addBanner(List<String> param) {
        List<BannerEntity> list = param.parallelStream().map(t -> {
            BannerEntity entity = new BannerEntity();
            entity.setBannerUrl(t);
            return entity;
        }).toList();
        return this.saveOrUpdateBatch(list);
    }

    public Boolean deleteBanner(String bannerId) {
        return this.removeById(bannerId);
    }

    public Page<BannerEntity> queryBanner() {
        return this.page(CommonPageRequestUtils.defaultPage());
    }
}
