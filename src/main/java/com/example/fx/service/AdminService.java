package com.example.fx.service;

import com.example.fx.dao.AdminDao;
import com.example.fx.pojo.Admin;

public class AdminService {

    /**
     * 管理员登录
     */
    public static boolean signIn(Admin admin) {
        Admin a = AdminDao.findAdminByNo(admin.getAdminNo());
        if (a.getPassword().equals(admin.getPassword()))
            return true;
        return false;
    }
}
