package com.jihao.service.impl;

import com.jihao.mapper.AdminMapper;
import com.jihao.pojo.Admin;
import com.jihao.pojo.AdminExample;
import com.jihao.service.AdminService;
import com.jihao.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Service
public class AdminServiceImpl implements AdminService {
    //数据访问层的对象
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        //根据传入的对象到数据库查询相应的对象
        //如果有条件，一定要创建AdminExample对象进行封装
        AdminExample example =  new AdminExample();
        //添加条件  a_name="name"
        example.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(example);
        if (list.size() > 0){
            //用户名唯一性
            Admin admin = list.get(0);
            //密码是以密文的发生进行存储，需要加密
            String mipwd = MD5Util.getMD5(pwd);
            if (mipwd.equals(admin.getaPass())){
                return admin;
            }
        }
        return null;
    }
}
