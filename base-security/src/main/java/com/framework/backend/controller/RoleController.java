package com.framework.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fucong
 * @since 2025/7/18 17:16
 * @description To do
 */
@RestController
@RequestMapping("/security/role")
@Tag(name = "RoleController", description = "角色管理(0101)")
public class RoleController {

  @GetMapping("test")
  public String test() {
    return "hello world";
  }
}
