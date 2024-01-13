package com.example.fx.test;

import com.example.fx.dao.LaboratoryDao;

import java.util.List;

class TestLab {
    public static void main(String[] args) {
        LaboratoryDao laboratoryDao = new LaboratoryDao();
        String school_name = "机械工程学院";

        try {
            List<String> labNumbers = laboratoryDao.findNobySchoolName(school_name);
            if (labNumbers.isEmpty()) {
                System.out.println("没有找到实验室号");
            } else {
                System.out.println("实验室号列表:");
                for (String labNumber : labNumbers) {
                    System.out.println(labNumber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
