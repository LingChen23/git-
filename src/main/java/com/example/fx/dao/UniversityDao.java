package com.example.fx.dao;

import com.example.fx.pojo.University;
import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;

public class UniversityDao {
    public static University findDate() {
        String sql = "select semester, year, month, day, status from localdate where status = true";
        University local = new University();
        try {
            ResultSet rs = JDBCUtil.execSql(sql);
            rs.next();
            local.setSemester(rs.getString("semester"));
            local.setYear(rs.getInt("year"));
            local.setMonth(rs.getInt("month"));
            local.setDay(rs.getInt("day"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return local;
    }
}
