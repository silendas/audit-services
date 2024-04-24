package com.cms.audit.api.Common.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalResponse {
    private String message;
    private Exception error;
    private Object data;
    private String errorMessage;
    private HttpStatus status;
}
