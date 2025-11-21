package com.framework.backend.attachment.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fucong
 * @description 文件操作接口
 * @since 2025/10/9 10:55
 */
public interface FileOperator {

  /**
   * 上传文件
   *
   * @param file
   * @param location
   */
  void upload(MultipartFile file, String location);

  void download(String attachmentId, HttpServletResponse response);
}
