package com.wwk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("1", "admin"),
    USER("2","user"),
    TEST("3","test");
    /**
     * 角色id
     */
    private final String roleId;
    /**
     * 描述
     */
    private final String name;
}
