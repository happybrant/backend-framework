package com.framework.backend.controller;

import com.framework.backend.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 * @since 2025/7/18 17:16
 * @description To do
 */
@RestController
@RequestMapping("/security/user")
@Tag(name = "UserController", description = "用户管理(0101)")
public class UserController {

  @PostMapping("/add")
  public void addUser(@RequestBody User user) {
    if (StringUtils.isNotBlank(user.getPassword())) {
      // 没有密码使用默认密码

    }
  }

  @PreAuthorize("hasAuthority('ROLE_admin')")
  @GetMapping("/getOne")
  public String getOne() {
    return "hello world！";
  }
}
