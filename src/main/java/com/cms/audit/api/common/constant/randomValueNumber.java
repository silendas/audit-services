package com.cms.audit.api.common.constant;

import java.util.Random;

public class randomValueNumber {

    public static Integer randomNumberGenerator(){
        Random rand = new Random();

        Integer random_value = rand.nextInt(1000)*2 + 1;
        Integer randomGet = random_value + rand.nextInt(1000);
        return randomGet;
    }
}
