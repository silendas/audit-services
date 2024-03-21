package com.cms.audit.api.Common.constant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class CreatePageRequest {
    public static Pageable createPageRequestUsing(int page, int size){
        return PageRequest.of(page, size);
    }
}
