package com.example.fx.test;

import com.example.fx.dao.ScheduleDao;
import com.example.fx.pojo.Schedule;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

class ScheduleDaoTest {
    public static void main(String[] args) {
        ScheduleDao scheduleDao = new ScheduleDao();

        // 测试根据条件获取课程信息
        int calender = 20;
        String semester = "2023-2024-1";
        String schoolName = "信息科学与工程学院";
        String labNo = "103";
        String majorName = "测控技术与仪器";
        String time = "3-4节";

        Map<String, Map<String, String>> scheduleMap = scheduleDao.getAllSchedule(calender, semester, schoolName, labNo);

        for (Map.Entry<String, Map<String, String>> entry : scheduleMap.entrySet()) {
            String dayOfWeek = entry.getKey();
            Map<String, String> timeInfoMap = entry.getValue();
            System.out.println(dayOfWeek + ":");
            for (Map.Entry<String, String> timeEntry : timeInfoMap.entrySet()) {
                System.out.println(timeEntry.getKey() + "\n" + timeEntry.getValue());
            }
        }

        LocalDate localDate = LocalDate.of(2024, 1, 6);
        Date date = java.sql.Date.valueOf(localDate);
        Schedule schedule = new Schedule("机械工程原理", "机械设计及其自动化",
                "机械工程学院", "2101", "001", "张老师", "103", "2023-2024-1", 20,
                date, "七八节", "无");
        // 调用 InsertSchedule 方法插入数据
        //boolean result = scheduleDao.InsertSchedule(schedule);

//        // 打印插入结果
//        if (result) {
//            System.out.println("插入成功！");
//        } else {
//            System.out.println("插入失败！");
//        }

        Map<String, Map<String, String>> schedulesMap = scheduleDao.findScheduleByMajor(calender, semester, schoolName, labNo, majorName);

        for (Map.Entry<String, Map<String, String>> entry : schedulesMap.entrySet()) {
            String dayOfWeek = entry.getKey();
            Map<String, String> timeInfoMap = entry.getValue();
            System.out.println(dayOfWeek + ":");
            for (Map.Entry<String, String> timeEntry : timeInfoMap.entrySet()) {
                System.out.println(timeEntry.getKey() + "\n" + timeEntry.getValue());
            }
        }

        boolean resultD = scheduleDao.deleteSchedule(calender, semester, schoolName, labNo, date, time);
        // 打印删除结果
        if (resultD) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败！");
        }
    }
}
