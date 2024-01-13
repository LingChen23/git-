package com.example.fx.test;

import com.example.fx.dao.TeacherDao;
import com.example.fx.pojo.Teacher;

class TeacherRegistrationTest {
    public static void main(String[] args) {
        // 创建教师对象
        Teacher teacher = new Teacher("002", "王老师", "123456", "机械工程学院");

        // 创建教师数据访问对象
        TeacherDao teacherDao = new TeacherDao();

        // 调用注册方法进行教师注册测试
        boolean registrationSuccess = teacherDao.addTeacher(teacher);
        if (registrationSuccess) {
            System.out.println("教师注册成功！");
        } else {
            System.out.println("教师注册失败，可能已存在相同的教师号。");
        }

        // 创建教师对象用于登录测试
        Teacher tea = new Teacher();
        tea.setTeacherNo("001"); // 替换为实际的教师号
        tea.setPassword("123456");   // 替换为实际的密码

        // 调用登录方法进行教师登录测试
        boolean loginResult = teacherDao.signUpTeacher(tea);

        // 根据登录结果输出信息
        if (loginResult) {
            System.out.println("教师登录成功！");
        } else {
            System.out.println("教师登录失败，用户名或密码错误。");
        }
    }
}
