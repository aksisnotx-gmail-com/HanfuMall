package com.app.domain.product.service;

import cn.hutool.core.util.StrUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.domain.product.mapper.ProductTypeMapper;
import com.sdk.util.asserts.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxl
 * @since 2024/4/1
 */
@Service
public class ProductTypeService extends AbstractService<ProductTypeMapper, ProductTypeEntity> {

    public List<ProductTypeEntity> getProductType(List<String> ids) {
        List<ProductTypeEntity> list = this.list(ProductTypeEntity::getId, ids);
        AssertUtils.assertTrue(list.size() == ids.size(), "商品类型不存在");
        return list;
    }

    public List<ProductTypeEntity> getAllType(String typeId) {
        return StrUtil.isBlank(typeId) ? this.list() : this.lambdaQuery().eq(ProductTypeEntity::getId, typeId).list();
    }

    public Boolean addType(ProductTypeEntity param) {
        return this.save(param);
    }

    public Boolean deleteType(String typeId) {
        return this.removeById(typeId);
    }

    public Boolean modifyType(ProductTypeEntity param) {
        ProductTypeEntity entity = this.getById(param.getId());
        entity.setType(param.getType());
        return this.updateById(entity);
    }
}
