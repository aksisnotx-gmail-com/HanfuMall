package com.app.domain.product.param;

import cn.hutool.core.bean.BeanUtil;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Data
@Schema(description = "商品详情参数")
public class ProductDetailParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 8571934096526803581L;

    //发货地址
    @Schema(description = "发货地址")
    @NotBlank(message = "发货地址不能为空")
    private String deliveryAddress;

    //轮播图来自于sku json形式 List<String>
    @Schema(description = "轮播图URL")
    @NotEmpty(message = "轮播图不能为空")
    private List<String> carousel;

    //商品名称
    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称")
    private String productName;

    //商品描述,图片都是为json
    @Schema(description = "商品描述")
    @NotEmpty(message = "商品描述不能为空")
    private List<String> descUrls;

    //商品类型：汉服、首饰
    @Schema(description = "商品类型URL")
    @NotEmpty(message = "商品类型不能为空")
    private List<ProductType> productTypeList;

    @Schema(description = "sku")
    @NotEmpty(message = "sku不能为空")
    private List<Sku> skus;

    /**
     * sku 尺码信息
     */
    @Schema(description = "尺码信息")
    @Data
    public static class Sku  implements Serializable {
        @Serial
        private static final long serialVersionUID = 5070470961911810329L;
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
        @Schema(description = "样式")
        private ProductStyle style;

        public static Sku create(ProductSkuEntity entity) {
            Sku skuEntity = new Sku();
            BeanUtil.copyProperties(entity,skuEntity);
            ProductStyle attribute = entity.getAttribute();
            skuEntity.setStyle(attribute);
            return skuEntity;
        }

    }

    @Schema(description = "样式类")
    @Data
    public static class ProductStyle  implements Serializable  {

        @Serial
        private static final long serialVersionUID = -936197310719796446L;

        //其他属性
        @Schema(description = "样式描述")
        private String desc;

        //图片
        @Schema(description = "图片")
        private String carouselUrl;
    }
}
