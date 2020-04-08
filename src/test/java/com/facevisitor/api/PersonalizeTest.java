package com.facevisitor.api;


import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.service.personalize.PersonalizeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersonalizeTest extends BaseTest {
    @Autowired
    PersonalizeService personalizeService;

    @Test
    public void 상품추천얻기() {
        List<GoodsDTO.GoodsListResponse> recommendations = personalizeService.getRecommendations(1L);
        System.out.println(recommendations);

    }

    @Test
    public void 이벤트() throws JsonProcessingException {
        personalizeService.viewEvent(1L, 2040L);
    }
}
