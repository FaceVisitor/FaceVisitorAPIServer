package com.facevisitor.api.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class Join {
    @NotNull(message = "이메일은 필수항목입니다.") @NotEmpty(message = "이메일이 비었습니다.")
    String email;
    @NotNull(message = "비밀번호는 필수항목입니다.") @NotEmpty(message = "비밀번호가 비었습니다.")
    String password;
    String name;
    String phone;
    @NotNull(message = "얼굴정보가  없습니다.")
    @NotEmpty(message = "얼굴정보가  없습니다.")
    List<String> faceIds;
    List<String> faceImageUrl;
    String gender;
    int lowAge;
    int highAge;
    Long storeId;

}
