package com.framework.backend.attachment.controller;

import com.framework.backend.attachment.service.FileOperator;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.common.result.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fucong
 * @description 附件控制器
 * @since 2025/10/10 14:44
 */
@RestController
@ResponseResult
@RequestMapping("/utility/attachment")
@Tag(name = "AttachmentController", description = "附件管理(0105)")
public class AttachmentController {

  @Resource(name = "localFileOperator")
  FileOperator fileOperator;

  @Operation(description = "附件上传")
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public void upload(@RequestParam("file") MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new BusinessException("文件为空！");
    }
    fileOperator.upload(file, "test");
  }

  @Operation(description = "附件下载")
  @RequestMapping(value = "/download", method = RequestMethod.GET)
  public void download(
      @RequestParam("attachmentId") String attachmentId, HttpServletResponse response) {
    fileOperator.download(attachmentId, response);
  }
}
