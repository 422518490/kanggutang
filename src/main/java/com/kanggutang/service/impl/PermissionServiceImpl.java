package com.kanggutang.service.impl;

import com.kanggutang.dao.PermissionMapperExt;
import com.kanggutang.dto.PermissionDTO;
import com.kanggutang.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapperExt permissionMapperExt;

    @Override
    public List<String> getFunctionList(Integer roleId) {
        return permissionMapperExt.getFunctionList(roleId);
    }
}
