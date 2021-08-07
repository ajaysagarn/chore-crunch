package com.ajsa.dyrepo.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomIdGenerator {

    /**
     * Generates a random alphanumeric string.
     * @return String
     */
    public static String getRandomNodeId(){
        return RandomStringUtils.randomAlphanumeric(8);
    }

}
