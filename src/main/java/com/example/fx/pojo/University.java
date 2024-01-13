package com.example.fx.pojo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class University {
    private String semester;
    private int year;
    private int month;
    private int day;
}
