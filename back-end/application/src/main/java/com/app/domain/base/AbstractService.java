package com.app.domain.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdk.util.asserts.AssertUtils;

import java.io.Serializable;

/**
 * @author xxl
 * @since 2024/3/19
 */
public abstract class AbstractService<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> implements IServiceEx<T> {
    public T getById(Serializable id,boolean isCheck) {
        T entity = super.getById(id);
        if (isCheck) {
            AssertUtils.notNull(entity, "记录不存在");
        }
        return entity;
    }

    @Override
    public T getById(Serializable id) {
        return this.getById(id, true);
    }
}
