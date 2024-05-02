package com.cms.audit.api.Common.constant;

import java.text.NumberFormat;

public class FormatNumber {
    
    public static String formatString(String input){
        double number = Double.parseDouble(input);
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(number);
    }

}
