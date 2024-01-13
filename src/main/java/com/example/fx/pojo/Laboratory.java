package com.example.fx.pojo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Laboratory {
    private String lab_no;
    private String school;
    private String status;
    private String capacity;
}

