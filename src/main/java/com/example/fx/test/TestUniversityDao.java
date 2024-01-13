package com.example.fx.test;

import com.example.fx.dao.UniversityDao;
import com.example.fx.pojo.University;

class TestUniversityDao {
    public static void main(String[] args) {
        UniversityDao UniversityDao = new UniversityDao();
        String semester = "2023-2024"; // 替换为实际的学期

        try {
            University University = UniversityDao.findDate();

            if (University != null) {
                System.out.println("Found date for semester " + semester + ": " + University);
            } else {
                System.out.println("No date found for semester " + semester);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
