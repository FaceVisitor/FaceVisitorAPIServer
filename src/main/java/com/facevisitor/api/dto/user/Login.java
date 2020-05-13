package com.facevisitor.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Login {

    @Data
    @AllArgsConstructor
    public static class Request{
        @NotEmpty(message = "얼굴을 인식하지 못했습니다.")
        List<String> faceId;

        public Request() {

        }
    }

    @Data
    public static class RequestTest{
        String email;
    }

    @Data
    @AllArgsConstructor
    public static class Response{
        String access_token;
        String refresh_token;
        String createdAt;
    }

    @Data
    @AllArgsConstructor
    public static class DirectResponse{
        String username;
        String access_token;
        String refresh_token;
        String createdAt;
    }


}
