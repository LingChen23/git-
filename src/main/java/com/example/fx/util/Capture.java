package com.example.fx.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Capture {

    public static String createRandomCode() {
        String[] strs = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(strs.length);
            code += strs[num];
        }
        return code;
    }

    public String ImageCode(char[] strs, int n) {
        int w = 180;
        int h = 50;
        int imageType = BufferedImage.TYPE_INT_RGB; //图片类型
        //1.画板 纸 JDK中提供画板类 Ctrl + p 查看快捷键参数
        BufferedImage image = new BufferedImage(w, h, imageType);
        //修改图片颜色
        //2.先获取画笔对象
        Graphics g = image.getGraphics();
        //画笔设置颜色
        g.setColor(Color.LIGHT_GRAY);
        //画填充矩形
        g.fillRect(0, 0, w, h); //全部填充

        //3.数据
        int x = 30;
        int y = 30;
        g.setColor(Color.blue);
        g.setFont(new Font("行楷", Font.PLAIN, 25));
        Random random = new Random();  //随机数
        for (int i = 0; i < n; i++) {
            //每循环一次取一个
            //生成随机数字
            //int num = random.nextInt(strs.length);
            String str = Character.toString(strs[i]);
            //每获取一个字符串，画上去

            g.drawString(str, x, y);
            x += 25;
        }
        //画一点干扰线
        g.setColor(Color.green);
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(30);
            int x2 = random.nextInt(50);
            int y1 = random.nextInt(30) + 120;
            int y2 = random.nextInt(50) + 20;
            g.drawLine(x1, x2, y1, y2);
        }

        // String captureName = "capture_" + System.currentTimeMillis() + ".jpg";
        String captureName = "capture" + ".jpg";
        String url = "src/main/resources/" + captureName;
        //5.把image生成到本地磁盘上
        try {   //未报告的异常错误java.io.IOException; 必须对其进行捕获或声明以便抛出
            ImageIO.write(image, "jpg", new File(url));
            return url;
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return null;
    }
}
