package com.app.domain.product.entity;

import cn.hutool.json.JSONUtil;
import com.app.domain.base.Entity;
import com.app.domain.product.enums.ProductType;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 商品详情(SysProductDetails)表实体类
 *
 * @author makejava
 * @since 2024-03-19 15:31:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品详情实体")
@TableName("sys_product_details")
public class ProductDetailsEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5910473738974612741L;

    //轮播图来自于sku json形式 List<String>
    @Schema(description = "轮播图")
    private String carousel;

    //发货地址
    @Schema(description = "发货地址")
    private String deliveryAddress;

    //商品名称
    @Schema(description = "商品名称")
    private String productName;

    //商品描述,图片都是为json List<String>
    @Schema(description = "商品描述")
    private String productDescription;

    //商品类型：汉服、首饰  List<ProductType>
    @Schema(description = "商品类型")
    private String productTypes;

    //折扣
    @Schema(description = "折扣")
    private Double discount;

    public void setProductDesc(List<String> descUrls) {
        this.productDescription = JSONUtil.toJsonStr(descUrls);
    }

    public List<String> getProductDesc() {
        return JSONUtil.toList(this.productDescription, String.class);
    }

    public void setProductType(List<ProductType> productTypes) {
        List<String> list = productTypes.parallelStream().map(ProductType::name).toList();
        this.productTypes = JSONUtil.toJsonStr(list);
    }

    public List<ProductType> getProductType() {
        return JSONUtil.toList(this.productTypes, String.class).parallelStream().map(ProductType::valueOf).toList();
    }

    public void setCarousels(List<String> carousel) {
        this.carousel = JSONUtil.toJsonStr(carousel);
    }

    public List<String> getCarousels() {
        return JSONUtil.toList(carousel,String.class);
    }
}

