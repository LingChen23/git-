package com.example.fx.test;

import com.example.fx.dao.MajorDao;

import java.util.List;

class MajorDaoTest {

    public static void main(String[] args) {
        MajorDao majorDao = new MajorDao();
        String schoolName = "信息科学与工程学院"; // 请替换为实际的学院编号
        List<String> majorNames = majorDao.findMajorNameBySchoolName(schoolName);
        System.out.println("Major Names:");
        for (String majorName : majorNames) {
            System.out.println(majorName);
        }
    }
}
