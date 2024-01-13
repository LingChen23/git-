package com.example.fx.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("12345a"));
    }

    public static String encrypt(String s) {
        return DigestUtils.md5Hex(s);
    }
}
