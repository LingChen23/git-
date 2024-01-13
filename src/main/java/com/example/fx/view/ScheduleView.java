package com.example.fx.view;

import com.example.fx.dao.*;
import com.example.fx.pojo.Schedule;
import com.example.fx.pojo.Teacher;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class ScheduleView extends BasicView {

    private final Label schoolLabel = new Label("学院：");

    private final ComboBox<String> schoolComBax = new ComboBox<>();

    private final Label majorLabel = new Label("专业：");

    private final ComboBox<String> majorComboBox = new ComboBox<>();

    private final Label classLabel = new Label("班级：");

    private final ComboBox<String> classComboBox = new ComboBox<>();

    private final Label courseName = new Label("课程：");
    private final ComboBox<String> courseComboBox = new ComboBox<>();

    private final Label content = new Label("备注: ");
    private final TextField note = new TextField();

    private final Button scheduleButton = new Button("排课");

    private final Button cancelScheduleButton = new Button("取消排课");

    private final AnchorPane anchorPane = new AnchorPane();

    private final Label Notice = new Label();

    private final TextField teacherText = new TextField();
    private final Label teacherLabel = new Label("教师工号: ");


    // 节次
    @Setter
    private String time;

    //  private boolean ifAdmin = false;

    // x月x号
    @Setter
    private Date date;

    public ScheduleView(boolean ifAdmin) {
        //this.ifAdmin = ifAdmin;
        anchorPane.getChildren().addAll(schoolLabel, schoolComBax, majorLabel, majorComboBox,
                classLabel, classComboBox, courseName, courseComboBox,
                content, note, scheduleButton, cancelScheduleButton, Notice);
        //System.out.println(ifAdmin);
        if (ifAdmin == true) {
            anchorPane.getChildren().addAll(teacherLabel, teacherText);
        }

        //学院下拉框
        SchoolDao schoolDao = new SchoolDao();
        List<String> schoolNames = schoolDao.getAllSchoolName();
        for (String s : schoolNames) {
            schoolComBax.getItems().add(s);
        }
        AnchorPane.setLeftAnchor(schoolLabel, 2.0);
        AnchorPane.setTopAnchor(schoolLabel, 3.0);
        AnchorPane.setLeftAnchor(schoolComBax, 40.0);
        schoolComBax.setPrefWidth(160);

        // 监听学院下拉框的变化，更新专业下拉框内容
        schoolComBax.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateMajorComboBox(newValue, majorComboBox);
        });
        AnchorPane.setLeftAnchor(majorLabel, 202.0);
        AnchorPane.setTopAnchor(majorLabel, 3.0);
        AnchorPane.setLeftAnchor(majorComboBox, 240.0);
        majorComboBox.setPrefWidth(180);

        majorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateClassComboBox(newValue, classComboBox);
            updateCourseComboBox(newValue, courseComboBox);
        });

        AnchorPane.setLeftAnchor(classLabel, 422.0);
        AnchorPane.setTopAnchor(classLabel, 3.0);
        AnchorPane.setLeftAnchor(classComboBox, 460.0);
        classComboBox.setPrefWidth(80);

        AnchorPane.setLeftAnchor(courseName, 2.0);
        AnchorPane.setTopAnchor(courseName, 43.0);
        courseName.setPrefWidth(40);
        AnchorPane.setLeftAnchor(courseComboBox, 40.0);
        AnchorPane.setTopAnchor(courseComboBox, 40.0);
        courseComboBox.setPrefWidth(160);
        AnchorPane.setLeftAnchor(content, 202.0);
        AnchorPane.setTopAnchor(content, 43.0);
        content.setPrefWidth(40.0);
        AnchorPane.setLeftAnchor(note, 240.0);
        AnchorPane.setTopAnchor(note, 40.0);
        note.setPrefWidth(100);
        AnchorPane.setLeftAnchor(scheduleButton, 102.0);
        AnchorPane.setTopAnchor(scheduleButton, 80.0);
        scheduleButton.setPrefWidth(100);

        AnchorPane.setTopAnchor(teacherLabel, 43.0);
        AnchorPane.setLeftAnchor(teacherLabel, 350.0);
        AnchorPane.setTopAnchor(teacherText, 43.0);
        AnchorPane.setLeftAnchor(teacherText, 400.0);
        teacherText.setPrefWidth(100);


        AnchorPane.setLeftAnchor(cancelScheduleButton, 300.0);
        AnchorPane.setTopAnchor(cancelScheduleButton, 80.0);
        cancelScheduleButton.setPrefWidth(100.0);

        AnchorPane.setLeftAnchor(Notice, 100.0);
        AnchorPane.setTopAnchor(Notice, 120.0);


        MainView mainView = (MainView) ViewFactory.createView(ViewType.MAIN);
        Notice.setStyle("-fx-text-fill: red;");
        Notice.setText("注意: 实验室可容纳" + LaboratoryDao.findCapacityByNo(
                mainView.getSchoolComboBox().getSelectionModel().getSelectedItem(),
                mainView.getLabComboBox().getSelectionModel().getSelectedItem()) + "人");

        scheduleButton.setOnMouseClicked(e -> {
            if (ifAdmin == true) {
                Teacher teacher;
                teacher = TeacherDao.findTeacherByNo(teacherText.getText());

                Schedule schedule = new Schedule(courseComboBox.getSelectionModel().getSelectedItem(),
                        majorComboBox.getSelectionModel().getSelectedItem(),
                        schoolComBax.getSelectionModel().getSelectedItem(),
                        classComboBox.getSelectionModel().getSelectedItem(),
                        teacher.getTeacherNo(), teacher.getTeacherName(),
                        mainView.getLabComboBox().getSelectionModel().getSelectedItem(),
                        mainView.getInitialDate().getSemester(),
                        mainView.getWeekComboBox().getSelectionModel().getSelectedItem(),
                        date, time, note.getText());
                ScheduleDao scheduleDao = new ScheduleDao();
                scheduleDao.InsertSchedule(schedule);
            } else {
                Teacher teacher = mainView.getTeacher();

                Notice.setStyle("-fx-text-fill: red;");
                Notice.setText("注意: 实验室可容纳" + LaboratoryDao.findCapacityByNo(
                        schoolComBax.getSelectionModel().getSelectedItem(),
                        mainView.getLabComboBox().getSelectionModel().getSelectedItem()) + "人");

                Schedule schedule = new Schedule(courseComboBox.getSelectionModel().getSelectedItem(),
                        majorComboBox.getSelectionModel().getSelectedItem(),
                        schoolComBax.getSelectionModel().getSelectedItem(),
                        classComboBox.getSelectionModel().getSelectedItem(),
                        teacher.getTeacherNo(), teacher.getTeacherName(),
                        mainView.getLabComboBox().getSelectionModel().getSelectedItem(),
                        mainView.getInitialDate().getSemester(),
                        mainView.getWeekComboBox().getSelectionModel().getSelectedItem(),
                        date, time, note.getText());
                ScheduleDao scheduleDao = new ScheduleDao();
                scheduleDao.InsertSchedule(schedule);

            }


            this.close();

            mainView.refresh();
        });


        cancelScheduleButton.setOnMouseClicked(e -> {
            //MainView mainView = (MainView) ViewFactory.createView(ViewType.MAIN);
            ScheduleDao.deleteSchedule(mainView.getWeekComboBox().getSelectionModel().getSelectedItem(),
                    mainView.getInitialDate().getSemester(),
                    mainView.getSchoolComboBox().getSelectionModel().getSelectedItem(),
                    mainView.getLabComboBox().getSelectionModel().getSelectedItem(),
                    date, time);

            this.close();

            mainView.refresh();
        });


        Scene scene = new Scene(anchorPane, 600, 200);
        setTitle("排课");
        setScene(scene);
    }

    private void updateCourseComboBox(String majorName, ComboBox<String> courseComboBox) {
        try {
            CourseMajorDao courseMajorDao = new CourseMajorDao();
            // 调用数据库操作方法获取该专业对应的课程列表
            List<String> className = courseMajorDao.findCourseNameByMajorName(majorName);
            courseComboBox.getItems().clear();
            courseComboBox.getItems().addAll(className);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateClassComboBox(String majorName, ComboBox<String> classComboBox) {
        try {
            ClassDao classDao = new ClassDao();
            // 调用数据库操作方法获取该专业对应的班级号列表
            List<String> className = classDao.findClassNameByMajorName(majorName);
            classComboBox.getItems().clear();
            classComboBox.getItems().addAll(className);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateMajorComboBox(String schoolName, ComboBox<String> majorComboBox) {
        try {
            MajorDao majorDao = new MajorDao();
            // 调用数据库操作方法获取该学院对应的专业号列表
            List<String> majorName = majorDao.findMajorNameBySchoolName(schoolName);
            majorComboBox.getItems().clear();
            majorComboBox.getItems().addAll(majorName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
