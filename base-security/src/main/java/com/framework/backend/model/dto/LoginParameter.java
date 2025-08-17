package com.framework.backend.model.dto;

import lombok.Data;

/**
 * @author fucong
 * @description 用来显示登录方法的swagger参数信息
 * @since 2025/08/16 22:59
 */
@Data
public class LoginParameter {
    private String account;
    private String password;
    private String loginType;
}
