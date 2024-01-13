package com.example.fx.dao;

import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LaboratoryDao {
    public static int findCapacityByNo(String schoolName, String labNo) {
        String sql = "select lab_capacity\n" +
                "from laboratory join school on\n" +
                "    laboratory.school_no = school.school_no\n" +
                "                where school_name = ? and lab_no = ?";
        try {
            ResultSet rs = JDBCUtil.execSql(sql, schoolName, labNo);
            while (rs.next()) {
                return rs.getInt("lab_capacity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<String> findNobySchoolName(String schoolName) {
        String sql = "select lab_no from laboratory join school on laboratory.school_no = school.school_no where school.school_name = ?";
        List<String> list = new ArrayList<>();

        try {
            ResultSet rs = JDBCUtil.execSql(sql, schoolName);
            while (rs.next()) {
                list.add(rs.getString("lab_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
