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
    @NotBlank(message = "username tidak boleh kosong")
    private String username;
    @NotBlank(message = "password tidak boleh kosong")
    @Size(min = 4, message = "Password harus setidaknya 8 character")
    private String password;
}
