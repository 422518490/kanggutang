package com.kanggutang.service;

import com.kanggutang.dto.UserDTO;
import com.kanggutang.model.User;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
public interface UserService {

    /**
     * 获取用户信息
     * @param userDTO
     * @return
     */
    public UserDTO getUser(UserDTO userDTO);

    public void addUser(User user);

    /**
     * 更新密码
     * @param userDTO
     */
    public void updatePwd(UserDTO userDTO);
}
