package com.facevisitor.api.dto.goods;

import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.domain.store.Store;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class GoodsDTO {


    @Data
    public static class GoodsJsonCreateRequest{
        String name;

        String brand;

        String price;

        String description;

        String img;

        Boolean active = true;

        Long category;

        Long store;
    }


    @Data
    public static class GoodsCreateRequest{
        String name;

        String vendor;

        BigDecimal price;

        BigDecimal salePrice;

        String description;

        Set<GoodsImage> images = new LinkedHashSet<>();

        Boolean active = true;

        Long category;

        Long store;
    }

    @Data
    public static class GoodsUpdateRequest{

        String name;

        String vendor;

        BigDecimal price;

        BigDecimal salePrice;

        String description;

        Boolean active;

        Long category;

        Long store;
    }

    @Data
    public static class GoodsDetailResponse{

        Long id;

        String name;

        String vendor;

        BigDecimal price;

        BigDecimal salePrice;

        String description;

        Boolean active;

        Store store;


        GoodsCategory category;

        Set<GoodsImage> images = new LinkedHashSet<>();

        BigDecimal qtyPrice = BigDecimal.ZERO;

        @Transient
        Boolean like;


    }

    @Data
    public static class GoodsListForCSVResponse {

        Long id;

        String name;

        String vendor;

        String category;

    }

    @Data
    public static class GoodsListResponse {

        Long id;

        String name;

        String vendor;

        String category;

        Set<GoodsImage> images = new LinkedHashSet<>();

        String description;

        BigDecimal price;

        BigDecimal salePrice;

        Boolean like;

    }


}
