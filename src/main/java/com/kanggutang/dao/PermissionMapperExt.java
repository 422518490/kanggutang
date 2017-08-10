package com.kanggutang.dao;

import com.kanggutang.dto.PermissionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
@Mapper
public interface PermissionMapperExt extends PermissionMapper {
    /**
     * 获取授权的方法集合
     * @param roleId
     * @return
     */
    public List<String> getFunctionList(Integer roleId);
}
