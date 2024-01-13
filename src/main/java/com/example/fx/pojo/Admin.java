package com.example.fx.pojo;

import com.example.fx.util.MD5Util;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor

@Builder
@ToString
public class Admin {
    private String adminNo;
    private String password;

    public Admin(String adminNo, String password) {
        this.adminNo = adminNo;
        this.password = MD5Util.encrypt(password);
    }
}

