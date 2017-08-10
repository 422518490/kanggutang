package com.kanggutang.dto;

import com.kanggutang.model.Permission;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
public class PermissionDTO extends Permission {

    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
