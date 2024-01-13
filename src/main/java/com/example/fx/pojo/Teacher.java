package com.example.fx.pojo;

import com.example.fx.util.MD5Util;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor

@Builder
@ToString
public class Teacher {
    private String teacherNo;
    private String teacherName;
    private String password;
    private String schoolName;

    public Teacher(String teacher_no, String teacher_name, String password, String schoolName) {
        this.teacherNo = teacher_no;
        this.teacherName = teacher_name;
        this.password = MD5Util.encrypt(password);
        this.schoolName = schoolName;
    }
}
