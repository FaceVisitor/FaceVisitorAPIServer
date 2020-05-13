package com.facevisitor.api.dto.user;

import com.facevisitor.api.domain.point.Point;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    @Data
    public static class UserListResponse{
        private Long id;
        private String name;
        private String phone;
    }

    @Data
    public static class MeResponseDTO {
        //보유 포인트
        BigDecimal point = BigDecimal.ZERO;
        List<Point> points = new ArrayList<>();
        private Long id;
        private String email;
        private String name;
        private String phone;
        private Boolean enable;


    }
}
