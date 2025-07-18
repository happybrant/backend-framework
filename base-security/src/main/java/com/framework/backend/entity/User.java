package com.framework.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.framework.backend.common.MyBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author fucong
 * @description 用户表
 * @since 2025/6/25 15:43
 */
@EqualsAndHashCode(callSuper = false)
@TableName("security_user")
@JsonFilter("security_user")
@Schema(description = "用户表")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({
  "password",
  "authorities",
  "enabled",
  "accountNonExpired",
  "accountNonLocked",
  "credentialsNonExpired"
})
public class User extends MyBaseEntity implements UserDetails {
  @Serial private static final long serialVersionUID = 1L;

  @Schema(description = "登录名")
  @TableField(value = "username", keepGlobalFormat = true)
  private String username;

  @Schema(description = "密码")
  @TableField(value = "password", keepGlobalFormat = true)
  private String password;

  @Schema(description = "用户头像")
  @TableField("avatar")
  private String avatar;

  @Schema(description = "姓名")
  @TableField("real_name")
  private String realName;

  @Schema(description = "邮箱")
  @TableField("email")
  private String email;

  @Schema(description = "备注")
  @TableField("remark")
  private String remark;

  @Schema(description = "状态")
  @TableField("status")
  private String status;

  @TableField(exist = false)
  private List<String> roleCodes;

  @Setter
  @TableField(exist = false)
  protected Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
