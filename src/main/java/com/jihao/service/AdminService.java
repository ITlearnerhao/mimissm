package com.jihao.service;

import com.jihao.pojo.Admin;

public interface AdminService {
    //登录功能
    Admin login(String name, String pwd);
}
