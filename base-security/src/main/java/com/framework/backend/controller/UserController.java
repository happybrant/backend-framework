package com.framework.backend.controller;

import com.framework.backend.annotation.Log;
import com.framework.backend.common.MyPage;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.dto.LoginParameter;
import com.framework.backend.model.dto.RoleUserDto;
import com.framework.backend.model.dto.UserRoleDto;
import com.framework.backend.model.entity.Role;
import com.framework.backend.model.entity.User;
import com.framework.backend.service.UserRoleRelService;
import com.framework.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@Slf4j
public class UserController {

  @Autowired private UserService userService;
  @Autowired private UserRoleRelService userRoleRelService;

  @Log(value = "用户登录", module = "用户管理")
  @Operation(
      description = "用户登录",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              content = {
                @Content(
                    mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    schema = @Schema(implementation = LoginParameter.class)),
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginParameter.class))
              }),
      responses = {
        @ApiResponse(
            responseCode = "200",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)))
      })
  @PostMapping(value = "/login")
  public void login() {
    // 不需要实现，由Spring Security实现，是用来生成Swagger API信息。
    log.debug("--------------login---------------");
  }

  @Log(value = "用户登出", module = "用户管理")
  @Operation(description = "用户登出")
  @PostMapping(value = "/logout")
  public void logout() {
    // 不需要实现，由Spring Security实现，是用来生成Swagger API信息。
    log.debug("--------------logout---------------");
  }

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

  @Log(value = "添加用户", module = "用户管理")
  @Operation(description = "添加用户")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/add")
  public void addUser(@RequestBody User user) {
    userService.addUser(user);
  }

  @Log(value = "修改用户信息", module = "用户管理")
  @Operation(description = "修改用户信息")
  @PostMapping("/update")
  public void updateUser(@RequestBody User user) {
    userService.updateUser(user);
  }

  @Log(value = "修改用户密码", module = "用户管理")
  @Operation(description = "修改用户密码")
  @PostMapping("/updatePwd")
  public void updatePwd(@RequestBody User user) {
    userService.updatePwd(user);
  }

  @Log(value = "重置用户密码", module = "用户管理")
  @Operation(description = "重置用户密码")
  @PostMapping("/resetPwd")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  public void resetPwd(@RequestBody User user) {
    userService.resetPwd(user);
  }

  @Log(value = "启用用户", module = "用户管理")
  @Operation(description = "启用用户")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/active")
  public void enable(@RequestBody User user) {
    userService.enableUser(user);
  }

  @Log(value = "用户登录", module = "用户管理")
  @Operation(description = "停用用户")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/disabled")
  public void disable(@RequestBody User user) {
    userService.disableUser(user);
  }

  @Operation(description = "获取角色下所有的用户")
  @PostMapping("/listUserByRole")
  public List<User> listUserByRole(@RequestBody Role role) {
    return userService.getUserListByRole(role);
  }

  @Log(value = "绑定用户下的角色", module = "用户管理")
  @Operation(description = "绑定用户下的角色")
  @PostMapping("/bindUserRole")
  public void bindUserRole(@RequestBody @Valid UserRoleDto userRoleDto) {
    userRoleRelService.bindUserRole(userRoleDto);
  }

  @Log(value = "绑定角色下的用户", module = "用户管理")
  @Operation(description = "绑定角色下的用户")
  @PostMapping("/bindRoleUser")
  public void bindRoleUser(@RequestBody @Valid RoleUserDto roleUserDto) {
    userRoleRelService.bindRoleUser(roleUserDto);
  }
}
