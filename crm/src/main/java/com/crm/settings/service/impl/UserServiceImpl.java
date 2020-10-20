package com.crm.settings.service.impl;

import com.crm.exception.LoginException;
import com.crm.settings.dao.UserDao;
import com.crm.settings.domain.User;
import com.crm.settings.service.UserService;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：周熙强
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("用户名不存在");
        }

        if (ip.contains(user.getAllowIps())){
            throw new LoginException("该ip不允许登录异常");
        }

        String currentTime = DateTimeUtil.getSysTime();
        String expireTime = user.getExpireTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }

        if (Integer.valueOf(user.getLockState()) != 1){
            throw new LoginException("账号已被锁定");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }

}
