package com.app.domain.product.entity;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "sys_product_details",autoResultMap = true)
public class ProductDetailsEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5910473738974612741L;

    //轮播图来自于sku json形式 List<String>
    @Schema(description = "轮播图")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> carousel;

    //发货地址
    @Schema(description = "发货地址")
    private String deliveryAddress;

    //商品名称
    @Schema(description = "商品名称")
    private String productName;

    //商品描述都是图片都是为json List<String>
    @Schema(description = "商品描述")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> descUrls;

    //商品类型：汉服、首饰  List<ProductType>
    @Schema(description = "商品类型ID")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> productTypeIds;
}

