package com.example.fx.view;

import com.example.fx.dao.TeacherDao;
import com.example.fx.pojo.Teacher;
import com.example.fx.util.Capture;
import com.example.fx.util.MD5Util;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.util.regex.Pattern;

public class LoginView extends BasicView {
    private final AnchorPane anchorPane = new AnchorPane();

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label capture = new Label("验证码");
    private final TextField captureText = new TextField();
    private final Button loginButton = new Button("登录");
    private final Canvas canvas = new Canvas(768, 576);
    private Label chineseText1 = new Label("账号");
    private Label chineseText2 = new Label("密码");

    private ImageView captureImageView;

    public LoginView() {
        setTitle("教师登录");
        initStyle(StageStyle.DECORATED);

        // Load image
        String imagePath = "register1.jpg";
        //  String imageCapturePath = "capture.jpg";

        InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath);
        if (imageStream != null) {
            Image backgroundImage = new Image(imageStream);
            ImageView backgroundImageView = new ImageView(backgroundImage);
            anchorPane.getChildren().add(backgroundImageView);
            AnchorPane.setTopAnchor(backgroundImageView, 0.0);
            AnchorPane.setLeftAnchor(backgroundImageView, 0.0);
        } else {
            System.out.println("错误：找不到图片文件");
        }

        captureImageView = new ImageView();
        captureImageView.setOnMouseClicked(e -> {
            refreshCaptureImage();
        });
        anchorPane.getChildren().add(captureImageView);
        AnchorPane.setTopAnchor(captureImageView, 320.9);
        AnchorPane.setLeftAnchor(captureImageView, 403.0);


        //账号密码
        chineseText1.setFont(Font.font("KaiTi", 18)); // 使用宋体字体，大小为24
        chineseText2.setFont(Font.font("KaiTi", 18)); // 使用宋体字体，大小为24
        capture.setFont(Font.font("KaiTi", 18));
        // 使用自定义的 RGB 颜色（红色）
        chineseText1.setTextFill(Color.rgb(27, 100, 173));
        // 使用自定义的 RGB 颜色（蓝色）
        chineseText2.setTextFill(Color.rgb(32, 107, 182));
        capture.setTextFill(Color.rgb(32, 107, 182));

        anchorPane.getChildren().add(chineseText1);
        AnchorPane.setTopAnchor(chineseText1, 229.4);
        AnchorPane.setLeftAnchor(chineseText1, 223.0);
        anchorPane.getChildren().add(chineseText2);
        AnchorPane.setTopAnchor(chineseText2, 279.9);
        AnchorPane.setLeftAnchor(chineseText2, 223.0);
        anchorPane.getChildren().add(capture);
        AnchorPane.setTopAnchor(capture, 327.9);
        AnchorPane.setLeftAnchor(capture, 207.0);

        // Add components to the anchor pane
        anchorPane.getChildren().addAll(usernameField, passwordField, loginButton);

        usernameField.setPrefWidth(245); // 设置宽度为200
        passwordField.setPrefWidth(245);
        captureText.setPrefWidth(145);
        loginButton.setPrefWidth(245);

        usernameField.setPrefHeight(32); // 设置高度为30
        passwordField.setPrefHeight(32);
        captureText.setPrefHeight(32);
        loginButton.setPrefHeight(32);


        AnchorPane.setTopAnchor(usernameField, 225.0);
        AnchorPane.setLeftAnchor(usernameField, 261.0);
        AnchorPane.setTopAnchor(passwordField, 275.0);
        AnchorPane.setLeftAnchor(passwordField, 261.0);
        anchorPane.getChildren().add(captureText);
        AnchorPane.setTopAnchor(captureText, 322.9);
        AnchorPane.setLeftAnchor(captureText, 261.0);
        AnchorPane.setTopAnchor(loginButton, 372.0);
        AnchorPane.setLeftAnchor(loginButton, 261.0);

        Scene adminLoginScene = new Scene(anchorPane, 768, 576);
        setScene(adminLoginScene);


        loginButton.setOnAction(event -> {
            if (checkUserInfo()) {
                Teacher teacher = TeacherDao.findTeacherByNo(usernameField.getText());
                if (teacher == null) {
                    showDialog("错误！教师不存在", Alert.AlertType.ERROR);
                }
                // System.out.println(teacher);
                if (teacher.getPassword().equals(MD5Util.encrypt(passwordField.getText()))) {
                    clearAll();

                    MainView mainView = (MainView) ViewFactory.createView(ViewType.MAIN);
                    mainView.setTeacher(teacher);
                    mainView.switchToLogin();

                    showDialog("登录成功", Alert.AlertType.INFORMATION);
                    ViewFactory.close(ViewType.LOGIN);
                    return;
                }
                showDialog("密码错误", Alert.AlertType.ERROR);
            }
        });
    }

    private void clearAll() {
        usernameField.setText("");
        passwordField.setText("");
        captureText.setText("");
    }

    private boolean checkUserInfo() {
        String account = usernameField.getText();   //获取用户输入账号
        String password = passwordField.getText();  //获取用户输入密码

        if (account.isEmpty() || password.isEmpty()) {
            if (account.isEmpty()) {
                showDialog("！未输入账号", Alert.AlertType.ERROR);
            }
            if (password.isEmpty()) {
                showDialog("！未输入密码", Alert.AlertType.ERROR);
            }
            return false;
        }
        String accountRegExp = "^\\d{5}$";  //限制账号为5位职工号
        String passwordReExp = "^[a-zA-Z0-9]{6,20}$";   //密码正则表达式
        if (!Pattern.matches(accountRegExp, account) || !Pattern.matches(passwordReExp, password)) {
            if (!Pattern.matches(accountRegExp, account)) {
                showDialog("！错误,账号是5位职工号", Alert.AlertType.ERROR);
            }
            if (!Pattern.matches(passwordReExp, password)) {
                showDialog("！错误,密码是长度在6-20位的英文字母和数字", Alert.AlertType.ERROR);
            }
            return false;
        }
        return true;
    }

    private void showDialog(String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void refreshCaptureImage() {
        String randomCode = Capture.createRandomCode();
        System.out.println("Generated random code: " + randomCode);
        // Testing ImageCode method
        char[] codeArray = randomCode.toCharArray();
        int n = codeArray.length;

        Capture captureCode = new Capture();
        String captureName = captureCode.ImageCode(codeArray, n);

        // 禁用缓存
        Image captureImage = new Image("file:" + captureName, false);
        captureImageView.setImage(captureImage);
    }
}
