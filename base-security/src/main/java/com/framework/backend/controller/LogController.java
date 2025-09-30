package com.framework.backend.controller;

import com.framework.backend.common.MyPage;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.model.entity.SecurityLog;
import com.framework.backend.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 * @since 2025/7/18 17:16
 * @description To do
 */
@ResponseResult
@RestController
@RequestMapping("/security/log")
@Tag(name = "LogController", description = "日志(0104)")
public class LogController {

  @Autowired LogService logService;

  @Operation(description = "分页查找日志")
  @PostMapping("/pageList")
  public MyPage<SecurityLog> pageList(@RequestBody SecurityLog log) {
    return logService.pageList(log);
  }
}
