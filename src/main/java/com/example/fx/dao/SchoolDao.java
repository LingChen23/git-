package com.example.fx.dao;

import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SchoolDao {
    public List<String> getAllSchoolName() {
        String sql = "select school_name from school";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JDBCUtil.execSql(sql);
            while (rs.next()) {
                list.add(rs.getString("school_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
