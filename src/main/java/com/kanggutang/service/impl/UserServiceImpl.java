package com.kanggutang.service.impl;

import com.kanggutang.dao.UserMapperExt;
import com.kanggutang.dto.UserDTO;
import com.kanggutang.model.User;
import com.kanggutang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapperExt userMapperExt;


    @Override
    public UserDTO getUser(UserDTO userDTO) {
        return userMapperExt.getUser(userDTO);
    }

    @Override
    public void addUser(User user) {
        userMapperExt.insertSelective(user);
    }

    @Override
    public void updatePwd(UserDTO userDTO) {
        userMapperExt.updatePwd(userDTO);
    }
}
