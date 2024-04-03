package com.app.domain.product.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.app.domain.base.Entity;
import com.app.domain.product.param.ProductDetailParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * (SysProductSku)表实体类
 *
 * @author makejava
 * @since 2024-03-19 16:41:50
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品sku")
@Data
@TableName(value = "sys_product_sku",autoResultMap = true)
public class ProductSkuEntity extends Entity {

    private static final Integer UN_RECOMMEND = 0;

    private static final Integer UN_SPECIAL = 0;

    @Serial
    private static final long serialVersionUID = -7506305583161118362L;

    //商品ID
    @Schema(description = "商品ID")
    private String productId;

    //价格
    @Schema(description = "价格")
    private BigDecimal price;

    //库存
    @Schema(description = "库存")
    private Integer stock;

    //尺码
    @Schema(description = "尺码")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> size;

    //颜色
    @Schema(description = "颜色")
    private String color;

    //其他属性
    @Schema(description = "其他属性")
    private String otherAttribute;

    //推荐
    @Schema(description = "是否推荐，1 是 0 不是")
    private Integer isRecommend;

    //特惠价格
    @Schema(description = "特惠价格")
    private BigDecimal specialPrice;

    //是否促销
    @Schema(description = "是否特惠，1 是 0 不是")
    private Integer isSpecial;

    public void setAttribute(ProductDetailParam.ProductStyle style) {
        this.otherAttribute = JSONUtil.toJsonStr(style);
    }

    public ProductDetailParam.ProductStyle getAttribute() {
        return JSONUtil.toBean(this.otherAttribute, ProductDetailParam.ProductStyle.class);
    }

    public static ProductSkuEntity create(ProductDetailParam.Sku sku) {
        ProductSkuEntity entity = new ProductSkuEntity();
        BeanUtil.copyProperties(sku,entity);
        entity.setAttribute(sku.getStyle());
        entity.setPrice(sku.getStyle().getPrice());
        entity.setIsRecommend(UN_RECOMMEND);
        entity.setIsSpecial(UN_SPECIAL);
        entity.setSpecialPrice(BigDecimal.ZERO);
        return entity;
    }
}

