package com.facevisitor.api.owner.dto;

import com.facevisitor.api.dto.image.ImageDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreDto {

    @Getter
    @Setter
    @ToString
    public static class StoreRequest{
        Long id;
        String name;
        String address;
        String openTime;
        String phone;
        String description;
        List<ImageDto> images = new ArrayList<>();
    }

    @Getter
    @Setter
    @ToString
    public static class StoreResponse{
        Long id;
        String name;
        String address;
        String description;
        List<String> images = new ArrayList<>();
    }
}
