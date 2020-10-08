package com.epoch.test.bean;

import lombok.Data;

/**
 * @author yanghui
 */
@Data
public class UserIn {
    private String username;
    private String password;
    private String name;
    private String gender;
    private Integer age;
    private String oldPassword;
    private String newPassword;
}
