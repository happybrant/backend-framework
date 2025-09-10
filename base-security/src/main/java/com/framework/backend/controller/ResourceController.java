package com.framework.backend.controller;

import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.Resource;
import com.framework.backend.service.ResourceService;
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
@ResponseResult
@RestController
@RequestMapping("/security/resource")
@Tag(name = "ResourceController", description = "资源管理(0103)")
public class ResourceController {

  @Autowired private ResourceService resourceService;

  @Operation(description = "添加资源")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/add")
  public void addResource(@Valid @RequestBody Resource resource) {
    resourceService.addResource(resource);
  }

  @Operation(description = "修改资源")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/update")
  public void updateResource(@RequestBody Resource resource) {
    resourceService.updateResource(resource);
  }

  @Operation(description = "批量删除资源")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/delete")
  public void deleteResource(@RequestBody List<String> ids) {
    resourceService.removeResourceByIds(ids);
  }

  @Operation(description = "启用资源")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/active")
  public void enable(@RequestBody Resource resource) {
    resourceService.enableResource(resource);
  }

  @Operation(description = "停用资源")
  @PreAuthorize("hasAuthority('ROLE_admin')")
  @PostMapping("/disabled")
  public void disable(@RequestBody Resource resource) {
    resourceService.disableResource(resource);
  }
}
