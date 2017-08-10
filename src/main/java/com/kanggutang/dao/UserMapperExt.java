package com.kanggutang.dao;

import com.kanggutang.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
@Mapper
public interface UserMapperExt extends UserMapper {
    /**
     * 获取用户信息
     * @param userDTO
     * @return
     */
    public UserDTO getUser(UserDTO userDTO);

    /**
     * 更新密码
     * @param userDTO
     */
    public void updatePwd(UserDTO userDTO);
}
