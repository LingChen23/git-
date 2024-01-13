package com.example.fx.dao;

import com.example.fx.pojo.Admin;
import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;

public class AdminDao {

    public static Admin findAdminByNo(String adminNo) {
        String sql = "select admin_no,password from admin where admin_no = ?";

        Admin admin = new Admin();
        try {
            ResultSet rs = JDBCUtil.execSql(sql, adminNo);
            if (rs.next()) {
                admin.setAdminNo(rs.getString("admin_no"));
                admin.setPassword(rs.getString("password"));
            } else {
                admin = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admin;
    }
}
