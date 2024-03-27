package com.cms.audit.api.Common.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class convertDateToRoman {

    public static String getRomanMonth(){
        Date date = new Date();
        LocalDate localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localdate.getMonthValue();
        String roman = "";
        if(month == 1){
            roman = "I";
        } else if(month == 2){
            roman = "II";
        } else if(month == 3){
            roman = "III";
        } else if(month == 4){
            roman = "IV";
        } else if(month == 5){
            roman = "V";
        } else if(month == 6){
            roman = "VI";
        } else if(month == 7){
            roman = "VII";
        } else if(month == 8){
            roman = "VIII";
        } else if(month == 9){
            roman = "IX";
        } else if(month == 10){
            roman = "X";
        } else if(month == 11){
            roman = "XII";
        } else if(month == 12){
            roman = "XII";
        }
        return roman;
    }

    public static Integer getIntYear(){
        Date date = new Date();
        LocalDate localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localdate.getYear();
        return year;
    }

    public static String convertDateToString(Date dt){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateToString = df.format(dt);
        return dateToString;
    }
    
}
