package com.framework.backend.model.vo;

import com.framework.backend.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucong
 * @since 2025/7/10 14:02
 * @description 登录返回信息
 */
@Data
@NoArgsConstructor
public class LoginResultObject {
  private User userInfo;
  private String token;
  private Long expireTime;
}
