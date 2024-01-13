package com.example.fx.test;

import com.example.fx.dao.SchoolDao;

import java.util.List;

class SchoolDaoTest {
    public static void main(String[] args) {
        SchoolDao schoolDao = new SchoolDao();
        List<String> schoolNames = schoolDao.getAllSchoolName();
        System.out.println("School Names:");
        for (String schoolName : schoolNames) {
            System.out.println(schoolName);
        }
    }
}
