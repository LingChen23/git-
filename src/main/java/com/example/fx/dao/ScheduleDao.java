package com.example.fx.dao;

import com.example.fx.pojo.Schedule;
import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 把所有数据提取出来，放到list数组中
 * 依据这些数据放到排课的网格中
 */
public class ScheduleDao {
    /**
     * 从数据库中获取课程信息显示在排课表中
     * <p>
     * 将数据库中date转化为星期，转化为字符串存储(例：MONDAY)
     * 参数： 周次，学期，学院名，实验室号
     * 返回： Map<星期,Map<第几节, 课程信息>>
     */
    public static Map<String, Map<String, String>> getAllSchedule(int calender, String semester, String schoolName, String labNo) {
        Map<String, Map<String, String>> map = new HashMap<>();
        String sql = "SELECT c.course_name, m.major_name, cl.class_name,\n" +
                "t.teacher_name, s.date, s.time, s.content\n" +
                "FROM schedule s\n" +
                "JOIN course c ON s.course_no = c.course_no\n" +
                "JOIN major m ON s.major_no = m.major_no\n" +
                "JOIN class cl ON s.class_no = cl.class_no\n" +
                //"AND major_no = (SELECT major_no FROM test_db_schedule.major WHERE major_name = '专业名')\n" +
                "JOIN teacher t ON s.teacher_no = t.teacher_no\n" +
                "JOIN school sc ON s.school_no = sc.school_no\n" +
                "WHERE s.calender = ? AND s.semester = ?\n" +
                "  AND sc.school_name = ? AND s.lab_no = ?\n" +
                "ORDER BY cl.class_name;";
        try {
            ResultSet rs = JDBCUtil.execSql(sql, calender, semester, schoolName, labNo);
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setCourseName(rs.getString("course_name"));
                schedule.setMajorName(rs.getString("major_name"));
                schedule.setClassName(rs.getString("class_name"));
                schedule.setTeacherName(rs.getString("teacher_name"));
                schedule.setDate(rs.getDate("date"));
                schedule.setTime(rs.getString("time"));
                schedule.setContent(rs.getString("content"));

                String dayOfWeek = getDayOfWeek(schedule.getDate());
                map.putIfAbsent(dayOfWeek, new HashMap<>());          // 星期key值传入
                Map<String, String> timeInfoMap = map.get(dayOfWeek); // 操作与特定星期几相关的Map

                if (timeInfoMap.containsKey(schedule.getTime())) {
                    String originalString = timeInfoMap.get(schedule.getTime()); //原始字符串
                    // 时间存在 进行分割字符串,是否找到相同老师名字
                    if (!originalString.contains(schedule.getTeacherName())) {
                        String delimiter = "\n课程：";                         // 指定的子字符串

                        // 使用 indexOf找到子字符串的位置
                        int splitIndex = originalString.indexOf(delimiter);

                        if (splitIndex != -1) { // 如果找到了子字符串
                            // 使用substring方法进行分割
                            String firstPart = originalString.substring(0, splitIndex);
                            String secondPart = originalString.substring(splitIndex + delimiter.length());

//                            System.out.println("第一部分: " + firstPart);
//                            System.out.println("第二部分: " + secondPart);
                            String first = firstPart.concat(" " + schedule.getTeacherName());
                            originalString = first.concat("\n" + secondPart);
                            //System.out.println("originalString:" + originalString);
                        }
                    }
                    if (!timeInfoMap.containsKey(schedule.getClassName())) {
                        //时间存在 进行分割字符串,是否名字
                        if (!timeInfoMap.get(schedule.getTime()).contains(schedule.getClassName())) {
                            String delimiter = "]";                          // 指定的子字符串
                            // 使用indexOf找到子字符串的位置
                            int splitIndex = originalString.indexOf(delimiter);

                            if (splitIndex != -1) { // 如果找到了子字符串
                                // 使用substring方法进行分割
                                String firstPart = originalString.substring(0, splitIndex);
                                String secondPart = originalString.substring(splitIndex + delimiter.length());

                                String first = firstPart.concat(" " + schedule.getClassName() + "]");
                                originalString = first.concat(secondPart);
                                timeInfoMap.put(schedule.getTime(), originalString);
                            }
                        }
                    }
                }
                /**
                 * 如果对应节没课添加daoMap
                 */
                else {
                    String detailsKey = "教师：" + schedule.getTeacherName() +
                            "\n课程：" + schedule.getCourseName() + "\n" +
                            schedule.getMajorName() + "\n[" + schedule.getClassName()
                            + "]\n备注：" + schedule.getContent();
                    // 添加信息到时间点对应的Map中
                    timeInfoMap.put(schedule.getTime(), detailsKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String getDayOfWeek(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        LocalDate localDate = LocalDate.parse(dateString);
        // 获取星期几
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        return String.valueOf(dayOfWeek.getValue());
    }

    /**
     * 专业查询
     *
     * @param calender
     * @param semester
     * @param schoolName
     * @param labNo
     * @param majorName
     * @return
     */
    public static Map<String, Map<String, String>> findScheduleByMajor(int calender, String semester, String schoolName, String labNo, String majorName) {
        Map<String, Map<String, String>> allSchedule = getAllSchedule(calender, semester, schoolName, labNo);
        Map<String, Map<String, String>> filteredSchedule = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> entry : allSchedule.entrySet()) {
            String dayOfWeek = entry.getKey();  //星期
            Map<String, String> timeInfoMap = entry.getValue();
            // 专业名匹配的记录
            Map<String, String> filteredTimeInfoMap = timeInfoMap.entrySet().stream()
                    .filter(scheduleEntry -> scheduleEntry.getValue().contains(majorName))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // 如果有匹配的记录，加入结果集
            if (!filteredTimeInfoMap.isEmpty()) {
                filteredSchedule.put(dayOfWeek, filteredTimeInfoMap);
            }
        }
        return filteredSchedule;
    }

    public static boolean InsertSchedule(Schedule schedule) {

        String schoolNo = " ", courseNo = " ", majorNo = " ", classNo = " ";
        java.sql.Date sqlDate = new java.sql.Date(schedule.getDate().getTime());
        System.out.println("========================" + sqlDate);
        String sql1 = "select  school_no from school where school_name = ?";
        try {
            ResultSet rs = JDBCUtil.execSql(sql1, schedule.getSchoolName());
            while (rs.next()) {
                schoolNo = rs.getString("school_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql2 = "select course_no from course where course_name = ?";
        try {
            ResultSet rs = JDBCUtil.execSql(sql2, schedule.getCourseName());
            while (rs.next()) {
                courseNo = rs.getString("course_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql3 = "select major_no from major where major_name = ?";
        try {
            ResultSet rs = JDBCUtil.execSql(sql3, schedule.getMajorName());
            while (rs.next()) {
                majorNo = rs.getString("major_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql4 = "select class_no from class where class_name = ?" +
                "  and major_no =\n" +
                "      (select major_no from test_db_schedule.major\n" +
                "               where major_name = ?)";
        try {
            ResultSet rs = JDBCUtil.execSql(sql4, schedule.getClassName(), schedule.getMajorName());
            while (rs.next()) {
                classNo = rs.getString("class_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql = "insert into schedule \n" +
                "    (course_no, major_no, class_no, teacher_no, time, date, semester, lab_no, calender, school_no, content) \n" +
                "            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        System.out.println("date:" + sqlDate);
        try {
            JDBCUtil.execSql(sql, courseNo, majorNo, classNo, schedule.getTeacherNo(), schedule.getTime(),
                    sqlDate, schedule.getSemester(), schedule.getLabNo(), schedule.getCalender(),
                    schoolNo, schedule.getContent());
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 管理员可删除任意课程 教师只可删除自己教的课程
     */
    public static boolean deleteSchedule(int calender, String semester, String schoolName, String labNo,
                                         Date date, String time) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String dayOfWeek = getDayOfWeek(date);

        String sql = "delete from schedule where calender = ? and semester = ?\n" +
                "and school_no = (select school_no from school where school_name = ?)\n" +
                "and lab_no = ? and date = ? AND time = ?";

        try {
            JDBCUtil.execSql(sql, calender, semester, schoolName, labNo, sqlDate, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
