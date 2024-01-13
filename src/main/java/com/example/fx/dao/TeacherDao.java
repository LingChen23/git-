package com.example.fx.dao;

import com.example.fx.pojo.Teacher;
import com.example.fx.util.JDBCUtil;

import java.sql.ResultSet;
import java.util.Objects;

public class TeacherDao {
    public static Teacher findTeacherByNo(String teacherNo) {
        String sql = "select teacher_no, teacher_name, password, school_name from teacher JOIN school on school.school_no = teacher.school_no where teacher_no = ?";

        Teacher teacher = new Teacher();
        try {
            ResultSet rs = JDBCUtil.execSql(sql, teacherNo);
            if (rs.next()) {
                teacher.setTeacherNo(rs.getString("teacher_no"));
                teacher.setTeacherName(rs.getString("teacher_name"));
                teacher.setPassword(rs.getString("password"));
                teacher.setSchoolName(rs.getString("school_name"));
            } else {
                teacher = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teacher;
    }

    /**
     * 添加教师
     *
     * @param teacher
     * @return 1:成功 0:失败
     */
    public static boolean addTeacher(Teacher teacher) {
        // 检查是否已经存在具有相同的
        if (findTeacherByNo(teacher.getTeacherNo()) != null) {
            return false; //教师存在
        }
        String teacherSchoolNo;

        String sql1 = "select school_no from school where school_name = ?";
        try {
            ResultSet rs = JDBCUtil.execSql(sql1, teacher.getSchoolName());
            rs.next();
            teacherSchoolNo = rs.getString("school_no");
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 添加管理员失败
        }

        String sql = "insert into teacher(teacher_no, teacher_name, " +
                "password, school_no) values(?,?,?,?)";
        try {
            JDBCUtil.execSql(sql, teacher.getTeacherNo(), teacher.getTeacherName(),
                    teacher.getPassword(), teacherSchoolNo);

            return true;
            // 成功添加
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 添加失败
        }
    }

    public static boolean signUpTeacher(Teacher teacher) {
        if (findTeacherByNo(teacher.getTeacherNo()) == null) {
            return false;
        }
        String sql = "select teacher_no,password from teacher where teacher_no = ? ";

        try {
            ResultSet rs = JDBCUtil.execSql(sql, teacher.getTeacherNo());
            rs.next();
            if (Objects.equals(teacher.getPassword(), rs.getString("password")))
                return true;
            System.out.println(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteTeacher(Teacher teacher) {
        String sql = "delete from teacher where teacher_no = ?";
        if (findTeacherByNo(teacher.getTeacherNo()) != null) {
            try {
                JDBCUtil.execSql(sql, teacher.getTeacherNo());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
