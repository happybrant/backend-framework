package com.framework.backend.controller;

import com.framework.backend.common.MyPage;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.Role;
import com.framework.backend.model.entity.User;
import com.framework.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 * @since 2025/7/18 17:16
 * @description To do
 */
@ResponseResult
@RestController
@RequestMapping("/security/user")
@Tag(name = "UserController", description = "用户管理(0101)")
public class UserController {

  @Autowired private UserService userService;

  @Operation(description = "分页查找用户")
  @PostMapping("/pageList")
  public MyPage<User> pageList(@RequestBody User user) {
    return userService.pageList(user);
  }

  @Operation(description = "获取单条记录")
  @PostMapping("/getOne")
  public User getOne(@RequestBody User user) {
    return userService.getOne(user);
  }

  @Operation(description = "添加用户")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/add")
  public void addUser(@RequestBody User user) {
    userService.addUser(user);
  }

  @Operation(description = "修改用户信息")
  @PostMapping("/update")
  public void updateUser(@RequestBody User user) {
    userService.updateUser(user);
  }

  @Operation(description = "修改用户密码")
  @PostMapping("/updatePwd")
  public void updatePwd(@RequestBody User user) {
    userService.updatePwd(user);
  }

  @Operation(description = "重置用户密码")
  @PostMapping("/resetPwd")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  public void resetPwd(@RequestBody User user) {
    userService.resetPwd(user);
  }

  @Operation(description = "获取角色下所有的用户")
  @PostMapping("/listUserByRole")
  public List<User> listUserByRole(@RequestBody Role role) {
    return userService.getUserListByRole(role);
  }
}
