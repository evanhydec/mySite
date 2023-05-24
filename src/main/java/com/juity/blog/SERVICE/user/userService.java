package com.juity.blog.SERVICE.user;

import com.juity.blog.POJO.user;

public interface userService {
    int updateUserInfo(user user);
    user login(String username,String pwd);
    user getUserInfoById(Integer id);
}
