package com.kanggutang.service;

import com.kanggutang.dto.PermissionDTO;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
public interface PermissionService {

    /**
     * 获取授权的方法集合
     * @param roleId
     * @return
     */
    public List<String> getFunctionList(Integer roleId);
}
