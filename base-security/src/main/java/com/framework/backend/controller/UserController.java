package com.framework.backend.controller;

import com.framework.backend.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 * @since 2025/7/18 17:16
 * @description To do
 */
@RestController
@RequestMapping("/security/user")
public class UserController {

  @PostMapping("/add")
  public void addUser(@RequestBody User user) {}

  @PreAuthorize("hasRole('Role_admin')")
  @GetMapping("/getOne")
  public String getOne(@RequestBody User user) {
    return "hello world！";
  }
}
