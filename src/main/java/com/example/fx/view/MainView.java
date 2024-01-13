package com.example.fx.view;

import com.example.fx.dao.LaboratoryDao;
import com.example.fx.dao.ScheduleDao;
import com.example.fx.dao.SchoolDao;
import com.example.fx.dao.UniversityDao;
import com.example.fx.pojo.Admin;
import com.example.fx.pojo.Teacher;
import com.example.fx.pojo.University;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class MainView extends BasicView {
    private final LocalDate currentDate = LocalDate.now(); // 获取当前日期

    private final AnchorPane anchorPane = new AnchorPane();

    private final GridPane gridPane = new GridPane();

    // 默认显示当前周
    private final UniversityDao university = new UniversityDao();

    // 使用数据库中的值初始化日期
    @Getter
    private final University initialDate = UniversityDao.findDate();

    private final LocalDate START_DATE = LocalDate.of(initialDate.getYear(),
            initialDate.getMonth(), initialDate.getDay());

    private final Integer DEFAULT_WEEK = calculateCurrentWeek(); // 计算当前周

    private final List<String> timeSlots = List.of("1-2节", "3-4节", "5-6节", "7-8节", "9-10节");


    private final Map<String, Label> labelMap = new HashMap<>();

    // 初始化周数
    @Getter
    private final ComboBox<Integer> weekComboBox = new ComboBox<>();

    @Getter
    private final ComboBox<String> schoolComboBox = new ComboBox<>();

    @Getter
    private final ComboBox<String> labComboBox = new ComboBox<>();

    private Label welcomeLabel = new Label();

    private Button loginButton = new Button("教师登录");

    private Button logoutButton = new Button("退出");

    @Setter
    @Getter
    private Teacher teacher;

    @Setter
    @Getter
    private Admin admin;
    private Button loginTeacherButton = new Button("注册教师");
    private Button cancelTeacherButton = new Button("删除教师");
    private Button adminloginButton = new Button("管理员登录");

    @Setter
    @Getter
    private boolean isLogin = false;

    @Setter
    @Getter
    private boolean isAdminLogin = false;


    public MainView() {

        anchorPane.getChildren().add(loginTeacherButton);
        anchorPane.getChildren().add(cancelTeacherButton);
        loginTeacherButton.setVisible(false);
        cancelTeacherButton.setVisible(false);
        AnchorPane.setTopAnchor(loginTeacherButton, 10.0);
        AnchorPane.setRightAnchor(loginTeacherButton, 200.0);
        AnchorPane.setTopAnchor(cancelTeacherButton, 10.0);
        AnchorPane.setRightAnchor(cancelTeacherButton, 120.0);

        anchorPane.getChildren().add(welcomeLabel);
        welcomeLabel.setVisible(false);
        AnchorPane.setTopAnchor(welcomeLabel, 13.0);
        AnchorPane.setRightAnchor(welcomeLabel, 100.0);

        // 初始化课程表
        initGrid();

        // 初始化周下拉框
        initWeekComboBox();

        initSchoolComboBox();


        // 设置点击事件
        anchorPane.getChildren().add(loginButton);
        AnchorPane.setTopAnchor(loginButton, 10.0);
        AnchorPane.setLeftAnchor(loginButton, 650.0);
        // 设置点击事件
        loginButton.setOnMouseClicked(e -> {
            ViewFactory.show(ViewType.LOGIN);
        });

        anchorPane.getChildren().add(adminloginButton);
        AnchorPane.setTopAnchor(adminloginButton, 10.0);
        AnchorPane.setLeftAnchor(adminloginButton, 750.0);
        //管理员点击事件
        adminloginButton.setOnMouseClicked(e -> {
            ViewFactory.show(ViewType.ADMIN);
        });

        anchorPane.getChildren().add(logoutButton);
        AnchorPane.setTopAnchor(logoutButton, 10.0);
        AnchorPane.setLeftAnchor(logoutButton, 650.0);
        logoutButton.setVisible(false);
        // 设置点击事件
        logoutButton.setOnMouseClicked(e -> {
            logout();
        });

        loginTeacherButton.setOnMouseClicked(e -> {
            ViewFactory.show(ViewType.TEACHER);
        });
        cancelTeacherButton.setOnMouseClicked(e -> {
            ViewFactory.show(ViewType.TEACHER);
        });

        Button queryButton = new Button("查询");
        anchorPane.getChildren().add(queryButton);
        AnchorPane.setTopAnchor(queryButton, 10.0);
        AnchorPane.setLeftAnchor(queryButton, 600.0);
        // 设置点击事件
        queryButton.setOnMouseClicked(event -> {
            refresh();
        });


        Scene scene = new Scene(anchorPane, 1000, 630);
        setTitle("排课");
        setScene(scene);
    }

    private int calculateCurrentWeek() {
        long daysBetween = ChronoUnit.DAYS.between(START_DATE, currentDate);
        int currentWeek = (int) (daysBetween / 7) + 1;
        return Math.max(1, currentWeek); // 确保不小于1
    }

    private void updateLabNumberComboBox(String selectedSchool, ComboBox<String> labNumberComboBox) {
        try {
            LaboratoryDao lab = new LaboratoryDao();
            // 调用数据库操作方法获取该学院对应的实验室号列表
            List<String> labNumbers = lab.findNobySchoolName(selectedSchool);
            labNumberComboBox.getItems().clear();
            labNumberComboBox.getItems().addAll(labNumbers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateGridPane(int selectedWeek, GridPane gridPane) {
        // 获取每周的开始日期
        LocalDate startDate = calculateDate(selectedWeek, DayOfWeek.MONDAY);

        // 更新 gridPane 中的日期标签
        for (int col = 1; col <= 7; col++) {
            Label dateLabel = (Label) getNodeFromGridPane(gridPane, col);
            dateLabel.setText(" " + getDayOfWeekString(col) + getFormattedDate(startDate) + "]");
            dateLabel.setStyle("-fx-pref-width: 110px; -fx-pref-height: 50px; -fx-border-color: black; -fx-border-width: 1 1 1 0; -fx-border-style: solid;");
            startDate = startDate.plusDays(1);
        }
    }

    // 根据周数和星期几算日期
    public LocalDate calculateDate(int weekNumber, DayOfWeek dayOfWeek) {
        LocalDate startDateOfWeek = START_DATE.plusWeeks(weekNumber - 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return startDateOfWeek.plusDays(dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == 0) {
                return node;
            }
        }
        return null;
    }

    private String getDayOfWeekString(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "星期一 [";
            case 2:
                return "星期二 [";
            case 3:
                return "星期三 [";
            case 4:
                return "星期四 [";
            case 5:
                return "星期五 [";
            case 6:
                return "星期六 [";
            case 7:
                return "星期日 [";
            default:
                return "";
        }
    }

    private String getFormattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日");
        return date.format(formatter);
    }


    private void initGrid() {   //1.画网格
        anchorPane.getChildren().add(gridPane);//距离顶部50像素
        AnchorPane.setTopAnchor(gridPane, 50.0);
        AnchorPane.setLeftAnchor(gridPane, 60.0); // 距离左侧100像素

        int numRows = 5;
        int numCols = 7;

        Label label = new Label();
        label.setStyle("-fx-pref-width: 110px; -fx-pref-height: 50px; -fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        gridPane.add(label, 0, 0);

        for (int i = 0; i < numCols; i++) {
            Label label1 = new Label();
            label1.setStyle("-fx-pref-width: 110px; -fx-pref-height: 50px; -fx-border-color: black; -fx-border-width: 1 1 1 0; -fx-border-style: solid;");
            gridPane.add(label1, i + 1, 0);
        }

        for (int i = 0; i < timeSlots.size(); i++) {
            Label label1 = new Label(timeSlots.get(i));
            label1.setStyle("-fx-alignment: center; -fx-pref-width: 110px; -fx-pref-height: 100px; -fx-border-color: black; -fx-border-width: 0 1 1 1; -fx-border-style: solid;");
            gridPane.add(label1, 0, i + 1);
        }

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Label label2 = new Label();
                label2.setStyle("-fx-font-size: 20px; -fx-pref-width: 110px; -fx-pref-height: 100px; -fx-border-color: black; -fx-border-width: 0 1 1 0; -fx-border-style: solid;");
                label2.setWrapText(true);
                labelMap.put(col + 1 + "-" + (row + 1), label2);
                gridPane.add(label2, col + 1, row + 1);
            }
        }
    }

    //2.周下拉框
    private void initWeekComboBox() {
        Text labelText = new Text("周：");
        anchorPane.getChildren().add(labelText);
        AnchorPane.setTopAnchor(labelText, 15.0);
        AnchorPane.setLeftAnchor(labelText, 50.0);

        List<Integer> list = new ArrayList<>(20);
        for (int i = 1; i <= 20; ++i) {
            list.add(i);
        }
        weekComboBox.setItems(FXCollections.observableList(list));
        anchorPane.getChildren().add(weekComboBox);
        weekComboBox.setValue(DEFAULT_WEEK);

        AnchorPane.setTopAnchor(weekComboBox, 10.0);
        AnchorPane.setLeftAnchor(weekComboBox, 80.0);

        updateGridPane(calculateCurrentWeek(), gridPane);
        // 监听周数下拉框的变化
        weekComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // 更新 gridPane 中的日期标签
            updateGridPane(newValue, gridPane);
        });
    }

    private void initSchoolComboBox() {
        Text labelText = new Text("学院：");
        anchorPane.getChildren().add(labelText);
        AnchorPane.setTopAnchor(labelText, 13.0);
        AnchorPane.setLeftAnchor(labelText, 190.0);

        SchoolDao schoolDao = new SchoolDao();
        List<String> schoolNames = schoolDao.getAllSchoolName();
        for (String s : schoolNames) {
            schoolComboBox.getItems().add(s);
        }

        schoolComboBox.setPrefWidth(160);
        schoolComboBox.setValue("信息科学与工程学院"); // 默认选中第一个学院
        anchorPane.getChildren().add(schoolComboBox);
        AnchorPane.setTopAnchor(schoolComboBox, 10.0); // 距离顶部10像素
        AnchorPane.setLeftAnchor(schoolComboBox, 226.0); // 距离

        // 实验室标签和下拉框
        Text labelLab = new Text("机房：");
        anchorPane.getChildren().add(labelLab);
        AnchorPane.setTopAnchor(labelLab, 13.0);
        AnchorPane.setLeftAnchor(labelLab, 440.0);

        anchorPane.getChildren().add(labComboBox);
        AnchorPane.setTopAnchor(labComboBox, 10.0);
        AnchorPane.setLeftAnchor(labComboBox, 480.0);
        updateLabNumberComboBox(schoolComboBox.getSelectionModel().getSelectedItem(), labComboBox);

        // 监听学院下拉框的变化，更新专业和实验室下拉框内容
        schoolComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            //updateMajorComboBox(newValue, majorComboBox);
            updateLabNumberComboBox(newValue, labComboBox);
        });
    }

    // 更新课表
    // 用map更新 1-1 表示1行1列格子
    private void search() {
        Map<String, Map<String, String>> schedule = ScheduleDao.getAllSchedule(weekComboBox.getSelectionModel().getSelectedItem(), initialDate.getSemester(), schoolComboBox.getSelectionModel().getSelectedItem(), labComboBox.getSelectionModel().getSelectedItem());

        Set<String> keySet = schedule.keySet(); //节次
        for (String key : keySet) {
            Map<String, String> sessionMap = schedule.get(key);
            Set<String> sessionSet = sessionMap.keySet();
            for (String session : sessionSet) {
                String ss = String.valueOf(timeSlots.indexOf(session) + 1);
                String newKey = key + "-" + ss;
                labelMap.get(newKey).setText(sessionMap.get(session));
            }
        }
    }

    // 清空label文本样式和事件响应
    private void clearLabelsText() {
        for (int i = 1; i <= 7; ++i) {
            for (int j = 1; j <= 5; ++j) {
                String key = String.valueOf(i) + '-' + j;
                labelMap.get(key).setText("");
                labelMap.get(key).setStyle("-fx-font-size: 11px; -fx-pref-width: 110px; -fx-pref-height: 100px; -fx-border-color: black; -fx-border-width: 0 1 1 0; -fx-border-style: solid;");
                labelMap.get(key).setOnMouseClicked(e -> {
                });
            }
        }
    }

    private void setLabelClickAble() {
        for (int i = 1; i <= 7; ++i) {
            for (int j = 1; j <= 5; ++j) {
                String key = String.valueOf(i) + '-' + j;
                Label label = labelMap.get(key);

                boolean isFutureDate = isFutureDate(weekComboBox.getSelectionModel().getSelectedItem(), DayOfWeek.of(i));
                if (isFutureDate) {
                    if (isAdminLogin) {
                        label.setOnMouseClicked(e -> {
                            ScheduleView scheduleView = (ScheduleView) ViewFactory.createView(ViewType.ADMIN_SCHEDULE);
                            LocalDate currentDate = calculateDate(weekComboBox.getSelectionModel().getSelectedItem(), DayOfWeek.of(Integer.parseInt(String.valueOf(key.charAt(0)))));
                            scheduleView.setDate(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            String time = timeSlots.get(key.charAt(2) - '1');
                            scheduleView.setTime(time);
                            ViewFactory.show(ViewType.ADMIN_SCHEDULE);
                        });
                    } else {
                        if (label.getText().isEmpty() || label.getText().contains(teacher.getTeacherName())) {
                            label.setOnMouseClicked(e -> {
                                ScheduleView scheduleView = (ScheduleView) ViewFactory.createView(ViewType.SCHEDULE);
                                LocalDate currentDate = calculateDate(weekComboBox.getSelectionModel().getSelectedItem(), DayOfWeek.of(Integer.parseInt(String.valueOf(key.charAt(0)))));
                                scheduleView.setDate(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                                String time = timeSlots.get(key.charAt(2) - '1');
                                scheduleView.setTime(time);
                                ViewFactory.show(ViewType.SCHEDULE);
                            });
                        }
                    }
                }
            }
        }
    }

    // 设置红色高亮可排课
    private void setTips() {
        for (int i = 1; i <= 7; ++i) {
            for (int j = 1; j <= 5; ++j) {
                String key = String.valueOf(i) + '-' + j;
                boolean isFutureDate = isFutureDate(weekComboBox.getSelectionModel().getSelectedItem(), DayOfWeek.of(i));
                if (isFutureDate && labelMap.get(key).getText().isEmpty()) {
                    labelMap.get(key).setText("可排课");

                    labelMap.get(key).setStyle("-fx-text-fill: red; -fx-font-size: 11px; -fx-pref-width: 110px; -fx-pref-height: 100px; -fx-border-color: black; -fx-border-width: 0 1 1 0; -fx-border-style: solid;");
                }
            }
        }
    }

    // 切换到登录态
    public void switchToLogin() {
        isLogin = true;
        loginButton.setVisible(false);
        logoutButton.setVisible(true);
        loginTeacherButton.setVisible(false);
        adminloginButton.setVisible(false);
        cancelTeacherButton.setVisible(false);
        refresh();
        welcomeLabel.setText(teacher.getSchoolName() + " " + teacher.getTeacherName() + " " + "老师 你好！");
        welcomeLabel.setVisible(true);
    }

    public void switchToAdminLogin() {
        isAdminLogin = true;
        adminloginButton.setVisible(false);
        loginButton.setVisible(false);
        loginTeacherButton.setVisible(true);
        cancelTeacherButton.setVisible(true);
        logoutButton.setVisible(true);

        refresh();
    }

    // 退出登录
    public void logout() {
        loginTeacherButton.setVisible(false);
        cancelTeacherButton.setVisible(false);
        logoutButton.setVisible(false);
        welcomeLabel.setText("");
        welcomeLabel.setVisible(false);
        loginButton.setVisible(true);
        adminloginButton.setVisible(true);
        isAdminLogin = false;
        isLogin = false;
        refresh();
    }

    public void refresh() {
        clearLabelsText();
        search();
        if (isLogin || isAdminLogin) {
            setLabelClickAble();
            setTips();
        }
    }

    // 判断是否是未来日期
    public boolean isFutureDate(int week, DayOfWeek dayOfWeek) {
        LocalDate currentDate = calculateDate(week, dayOfWeek);
        return currentDate.isAfter(LocalDate.now());
    }
}
