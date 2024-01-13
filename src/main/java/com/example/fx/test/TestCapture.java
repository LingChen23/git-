package com.example.fx.test;

import com.example.fx.util.Capture;

public class TestCapture {
    public static void main(String[] args) {
        // Testing createRandomCode method
        String randomCode = Capture.createRandomCode();
        System.out.println("Generated random code: " + randomCode);

        // Testing ImageCode method
        char[] codeArray = randomCode.toCharArray();
        int n = codeArray.length;

        Capture capture = new Capture();
        capture.ImageCode(codeArray, n);

        // System.out.println("Image generated and saved to D:\\2\\aaa.jpg");
    }
}
