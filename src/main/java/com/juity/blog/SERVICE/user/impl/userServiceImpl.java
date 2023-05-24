package com.juity.blog.SERVICE.user.impl;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.DAO.userDao;
import com.juity.blog.DTO.cond.userCond;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.user;
import com.juity.blog.SERVICE.user.userService;
import com.juity.blog.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    @Autowired
    private userDao userDao;

    @Override
    public int updateUserInfo(user user) {
        return userDao.updateUser(user);
    }

    @Override
    public user login(String username, String pwd) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(pwd))
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);

        String p = TaleUtils.MD5encode(username + pwd);
        user user = userDao.getUserByCond(new userCond(username,p));
        if (null == user)
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        return user;
    }

    @Override
    public user getUserInfoById(Integer id) {
        if (null == id)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return userDao.getUserById(id);
    }
}
