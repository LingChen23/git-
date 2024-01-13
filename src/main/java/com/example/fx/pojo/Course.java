package com.example.fx.pojo;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Course {
    private String courseNo;
    private String courseName;
    private String major;
    private String grade;
    private String teacherName;
    private String time;    //一二节、三四节
    private Date date;
    private String calender;    //校历第几周
    private String content;

    public static void main(String[] args) {
        CourseBuilder c = new CourseBuilder()
                .courseName("dda")
                .courseNo("da")
                .grade("sq");
        Course cc = c.build();
        System.out.println(cc);
    }
}