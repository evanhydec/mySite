package com.example.site.SERVICE.user;

import com.example.site.POJO.user;
import org.apache.ibatis.annotations.Param;

public interface userService {
    int updateUserInfo(user user);
    user login(String username,String pwd);
    user getUserInfoById(Integer id);
}
