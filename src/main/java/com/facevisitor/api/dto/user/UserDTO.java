package com.facevisitor.api.dto.user;

import com.facevisitor.api.domain.face.FaceMeta;
import com.facevisitor.api.domain.point.Point;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    @Data
    public static class UserListResponse {
        private Long id;
        private String name;
        private String phone;
        private LocalDateTime createdAt;
    }

    @Data
    public static class UserDetailResponse {
        private Long id;
        private String name;
        private String phone;
        private FaceMeta faceMeta;
        private String email;
        private BigDecimal point;
        private LocalDateTime createdAt;
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
