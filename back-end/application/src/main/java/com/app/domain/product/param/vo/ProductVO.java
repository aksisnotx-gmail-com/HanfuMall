package com.app.domain.product.param.vo;

import cn.hutool.core.bean.BeanUtil;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.enums.ProductType;
import com.app.domain.product.param.ProductDetailParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @since 2024/3/20
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品VO")
public class ProductVO extends Entity {

    private static final String SIZE = "size";
    private static final String STYLE = "style";

    @Serial
    private static final long serialVersionUID = -5972103772272222801L;

    //轮播图来自于
    @Schema(description = "轮播图")
    private List<String> carousel;

    //商品类型：汉服、首饰
    @Schema(description = "商品类型")
    private List<ProductType> productTypes;

    //商品描述,图片都是为json
    @Schema(description = "商品描述")
    private List<String> descUrls;

    //发货地址
    @Schema(description = "发货地址")
    private String deliveryAddress;

    //商品名称
    @Schema(description = "商品名称")
    private String productName;

    //折扣
    @Schema(description = "折扣")
    private Double discount;

    /**
     * 可选列表
     */
    @Schema(description = "可选列表")
    private Map<String,List<Object>>  specList;

    /**
     * 可供选择的规格组合
     */
    @Schema(description = "可供选择的规格组合")
    private List<SkuVO> specCombinationList;

    public static ProductVO create(ProductDetailsEntity entity,List<ProductSkuEntity> skuEntities){
        ProductVO vo = new ProductVO();
        BeanUtil.copyProperties(entity,vo);
        //设置sku
        List<SkuVO> list = skuEntities.parallelStream().map(SkuVO::create).toList();
        //设置可供选择的规格组合
        vo.setSpecCombinationList(list);
        //设置可选列表
        List<String> sizes = skuEntities.parallelStream().map(ProductSkuEntity::getSize).distinct().toList();

        //样式去重
        List<ProductDetailParam.ProductStyle> styles = skuEntities.
                parallelStream().
                map(ProductSkuEntity::getOtherAttribute).
                distinct().
                map(t -> { ProductSkuEntity sku = new ProductSkuEntity();
                    sku.setOtherAttribute(t);
            return sku;
        }).map(ProductSkuEntity::getAttribute).toList();
        HashMap<String,List<Object>> map = new HashMap<>();
        map.put(SIZE, Collections.singletonList(sizes));
        map.put(STYLE, Collections.singletonList(styles));
        vo.setSpecList(map);
        return vo;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SkuVO extends Entity implements Serializable {

        @Serial
        private static final long serialVersionUID = -3713385444355670804L;

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

        //其他属性
        @Schema(description = "样式描述")
        private String desc;

        //图片
        @Schema(description = "图片")
        private String carouselUrl;

        public static SkuVO create(ProductSkuEntity productSku) {
            SkuVO skuVO = new SkuVO();
            BeanUtil.copyProperties(productSku,skuVO);
            ProductDetailParam.ProductStyle attribute = productSku.getAttribute();
            skuVO.setDesc(attribute.getDesc());
            skuVO.setCarouselUrl(attribute.getCarouselUrl());
            return skuVO;
        }
    }
}
