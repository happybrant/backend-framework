package com.framework.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

/**
 * @author fucong
 * @description 用户角色关联关系
 * @since 2025/08/16 22:59
 */
@Data
public class UserRoleDto {
  @NotBlank(message = "用户id不能为空")
  private String userId;

  private List<String> roleIds;
}
