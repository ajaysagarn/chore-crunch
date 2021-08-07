package com.ajsa.dyrepo.util;

public class RepositoryUtils {

    public static boolean isNodePathNodeId(String nodePath){
        if(nodePath.contains("/")) return false;
        return true;
    }

    public static String trimSlashes(String nodePath){
        if(nodePath.endsWith("/")){
            nodePath = nodePath.substring(0, nodePath.length() - 1);
        }

        return nodePath;
    }

}
