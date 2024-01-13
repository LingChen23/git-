package com.example.fx.dao;

import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MajorDao {
    public List<String> findMajorNameBySchoolName(String schoolName) {
        String sql = "select major_name from major " +
                "join school on major.school = school.school_no " +
                "where school.school_name = ?";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JDBCUtil.execSql(sql, schoolName);
            while (rs.next()) {
                list.add(rs.getString("major_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

