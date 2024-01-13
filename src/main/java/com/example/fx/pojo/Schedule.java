package com.example.fx.pojo;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Schedule {
    private String courseName;
    private String majorName;
    private String schoolName;
    private String className;
    private String teacherNo;
    private String teacherName;
    private String labNo;
    private String semester;
    private int calender;
    private Date date;
    // 节次
    private String time;
    private String content;
}