package com.cms.audit.api.Authentication.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SigninDTO {
    @NotBlank(message = "username should not be empty")
    private String username;
    @NotBlank(message = "username should not be empty")
    @Size(min = 8, message = "Password should be atleast 8 char")
    private String password;
}
