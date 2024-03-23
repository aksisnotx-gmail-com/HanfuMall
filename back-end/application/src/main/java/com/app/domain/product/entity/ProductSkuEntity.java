package com.app.domain.product.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.app.domain.base.Entity;
import com.app.domain.product.param.ProductDetailParam;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * (SysProductSku)表实体类
 *
 * @author makejava
 * @since 2024-03-19 16:41:50
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品sku")
@Data
@TableName("sys_product_sku")
public class ProductSkuEntity extends Entity {

    @Serial
    private static final long serialVersionUID = -7506305583161118362L;

    //商品ID
    @Schema(description = "商品ID")
    private String productId;

    //价格
    @Schema(description = "价格")
    private Double price;

    //库存
    @Schema(description = "库存")
    private Integer stock;

    //尺码
    @Schema(description = "尺码")
    private String size;

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
    private Double specialPrice;

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
        return entity;
    }
}

