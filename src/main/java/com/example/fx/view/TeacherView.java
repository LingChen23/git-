package com.example.fx.view;

import com.example.fx.dao.SchoolDao;
import com.example.fx.dao.TeacherDao;
import com.example.fx.pojo.Teacher;
import com.example.fx.util.MD5Util;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.regex.Pattern;

public class TeacherView extends BasicView {
    private final Label noLabel = new Label("教师工号：");
    private final TextField noText = new TextField();
    private final Label nameLabel = new Label("教师姓名：");
    private final TextField nameText = new TextField();
    private final Label schoolLabel = new Label("学院：");
    private final ComboBox<String> schoolComboBox = new ComboBox<>();
    private final Label passwordLabel = new Label("密码：");
    private final PasswordField passwordField = new PasswordField();

    private final Button signUpTeacherButton = new Button("注册教师");
    private final Button deleteTeacherButton = new Button("移除教师");

    private final AnchorPane anchorPane = new AnchorPane();

    public TeacherView() {
        setTitle("教师信息管理");
        initStyle(StageStyle.DECORATED);

        anchorPane.getChildren().addAll(schoolLabel, schoolComboBox,
                signUpTeacherButton, deleteTeacherButton,
                noLabel, noText, nameLabel, nameText, passwordLabel,
                passwordField);

        //学院下拉框
        SchoolDao schoolDao = new SchoolDao();
        List<String> schoolNames = schoolDao.getAllSchoolName();
        for (String s : schoolNames) {
            schoolComboBox.getItems().add(s);
        }

        AnchorPane.setTopAnchor(schoolLabel, 13.0);
        AnchorPane.setLeftAnchor(schoolLabel, 10.0);

        AnchorPane.setTopAnchor(schoolComboBox, 10.0);
        AnchorPane.setLeftAnchor(schoolComboBox, 40.0);
        schoolComboBox.setPrefWidth(160);

        AnchorPane.setTopAnchor(noLabel, 13.0);
        AnchorPane.setLeftAnchor(noLabel, 210.0);
        AnchorPane.setTopAnchor(noText, 10.0);
        AnchorPane.setLeftAnchor(noText, 270.0);
        noText.setPrefWidth(70);
        AnchorPane.setTopAnchor(nameLabel, 13.0);
        AnchorPane.setLeftAnchor(nameLabel, 350.0);
        AnchorPane.setTopAnchor(nameText, 10.0);
        AnchorPane.setLeftAnchor(nameText, 410.0);
        nameText.setPrefWidth(80);

        AnchorPane.setTopAnchor(passwordLabel, 13.0);
        AnchorPane.setLeftAnchor(passwordLabel, 500.0);
        AnchorPane.setTopAnchor(passwordField, 10.0);
        AnchorPane.setLeftAnchor(passwordField, 538.0);
        passwordField.setPrefWidth(80);

        AnchorPane.setTopAnchor(signUpTeacherButton, 43.0);
        AnchorPane.setLeftAnchor(signUpTeacherButton, 200.0);
        AnchorPane.setTopAnchor(deleteTeacherButton, 43.0);
        AnchorPane.setLeftAnchor(deleteTeacherButton, 300.0);

        Scene teacherScene = new Scene(anchorPane, 630, 80);
        setScene(teacherScene);

        signUpTeacherButton.setOnAction(event -> {
            ViewFactory.show(ViewType.TEACHER);
            if (checkUserInfo()) {  //获取教师工号
                Teacher teacher = TeacherDao.findTeacherByNo(noText.getText());
                if (teacher != null) {  //判断教师是否已经存在
                    showDialog("注册失败！教师已经存在", Alert.AlertType.ERROR);
                } else {
                    Teacher teacher1 = new Teacher();

                    teacher1.setSchoolName(schoolComboBox.getSelectionModel().getSelectedItem());
                    teacher1.setTeacherNo(noText.getText());
                    teacher1.setTeacherName(nameText.getText());
                    teacher1.setPassword(MD5Util.encrypt(passwordField.getText()));
                    TeacherDao.addTeacher(teacher1);
                    clearAll();

                    showDialog("注册成功", Alert.AlertType.INFORMATION);
                }
            }
        });


        deleteTeacherButton.setOnAction(event -> {
            ViewFactory.show(ViewType.TEACHER);
            if (checkUserInfo()) {
                Teacher teacher = TeacherDao.findTeacherByNo(noText.getText());
                if (teacher == null) {
                    showDialog("移除失败！教师不存在", Alert.AlertType.ERROR);
                }

                // System.out.println(teacher);
                else {
                    teacher.setSchoolName(schoolComboBox.getSelectionModel().getSelectedItem());
                    teacher.setTeacherNo(noText.getText());
                    teacher.setTeacherName(nameText.getText());
                    teacher.setPassword(MD5Util.encrypt(passwordField.getText()));
                    TeacherDao.deleteTeacher(teacher);

                    clearAll();
                    showDialog("移除成功", Alert.AlertType.INFORMATION);
                }
            }
        });
    }

    private void clearAll() {
        noText.setText("");
        nameText.setText("");
        passwordField.setText("");
    }


    private boolean checkUserInfo() {
        String teacherNo = noText.getText();
        if (teacherNo.isEmpty()) {
            showDialog("！未输入教师工号", Alert.AlertType.ERROR);
            return false;
        }
        String accountRegExp = "^\\d{5}$";
        if (!Pattern.matches(accountRegExp, teacherNo)) {
            if (!Pattern.matches(accountRegExp, teacherNo)) {
                showDialog("！错误,教师工号是5位", Alert.AlertType.ERROR);
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
}
