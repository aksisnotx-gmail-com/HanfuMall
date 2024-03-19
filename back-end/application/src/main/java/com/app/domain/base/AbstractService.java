package com.app.domain.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author xxl
 * @since 2024/3/19
 */
public class AbstractService<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> implements IServiceEx<T> {

}
