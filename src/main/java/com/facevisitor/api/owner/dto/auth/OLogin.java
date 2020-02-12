package com.facevisitor.api.owner.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OLogin {

    @NotNull @NotEmpty
    String email;
    @NotNull @NotEmpty
    String password;
}
