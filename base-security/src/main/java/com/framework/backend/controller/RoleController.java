package com.framework.backend.controller;

import com.framework.backend.model.entity.Role;
import com.framework.backend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 * @since 2025/7/18 17:16
 * @description To do
 */
@RestController
@RequestMapping("/security/role")
@Tag(name = "RoleController", description = "角色管理(0101)")
public class RoleController {

  @Autowired private RoleService roleService;

  @Operation(description = "添加角色")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/add")
  public void addRole(@Valid @RequestBody Role role) {
    roleService.addRole(role);
  }

  @Operation(description = "修改角色")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/update")
  public void updateRole(@RequestBody Role role) {
    roleService.updateRole(role);
  }

  @Operation(description = "批量删除角色")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/delete")
  public void deleteRole(@RequestBody List<String> ids) {
    roleService.removeRoleByIds(ids);
  }
}
