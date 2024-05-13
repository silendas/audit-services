package com.cms.audit.api.Common.constant;

import java.text.NumberFormat;

public class FormatNumber {
    
    public static String formatString(Long input) {
        long number = input;
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(number);
    }

}
