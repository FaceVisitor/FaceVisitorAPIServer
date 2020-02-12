package com.facevisitor.api.owner.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OJoin {
    @NotNull(message = "이메일은 필수항목입니다.") @NotEmpty(message = "이메일이 비었습니다.")
    String email;
    @NotNull(message = "비밀번호는 필수항목입니다.") @NotEmpty(message = "비밀번호가 비었습니다.")
    String password;
    String name;
    String phone;
}

