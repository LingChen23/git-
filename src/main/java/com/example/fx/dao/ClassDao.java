package com.example.fx.dao;

import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClassDao {
    public List<String> findClassNameByMajorName(String majorName) {
        String sql = "select class_name from class " +
                "join major on class.major_no = major.major_no " +
                "where major.major_name = ?";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JDBCUtil.execSql(sql, majorName);
            while (rs.next()) {
                list.add(rs.getString("class_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}