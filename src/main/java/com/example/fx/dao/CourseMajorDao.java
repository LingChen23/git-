package com.example.fx.dao;

import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseMajorDao {
    public List<String> findCourseNameByMajorName(String majorName) {
        String sql = "SELECT c.course_name\n" +
                "FROM major m\n" +
                "JOIN course_major cm ON m.major_no = cm.major_no\n" +
                "JOIN course c ON cm.course_no = c.course_no\n" +
                "WHERE m.major_name = ?;\n";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JDBCUtil.execSql(sql, majorName);
            while (rs.next()) {
                list.add(rs.getString("course_name"));
                System.out.println(rs.getString("course_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
