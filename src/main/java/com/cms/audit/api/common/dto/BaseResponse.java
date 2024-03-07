package com.cms.audit.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private T data;
    private Meta meta;

    public BaseResponse() {
    }

    public Object getCustomizeResponse(String key) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", Map.of(key, data));
        result.put("meta", meta);
        return result;
    }

    public boolean hasError() {
        return meta != null && meta.getStatusCode() >= 400;
    }


}
