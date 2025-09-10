package com.framework.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

/**
 * @author fucong
 * @description 角色资源关联关系
 * @since 2025/08/16 22:59
 */
@Data
public class RoleResourceDto {
  @NotBlank(message = "角色id不能为空")
  private String roleId;

  private List<String> resourceIds;
}
