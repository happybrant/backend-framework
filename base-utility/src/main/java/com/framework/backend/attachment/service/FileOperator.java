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
   * @param file 文件
   * @return 文件id
   */
  String upload(MultipartFile file);

  /**
   * 下载文件
   *
   * @param attachmentId 文件id
   * @param response 响应
   */
  void download(String attachmentId, HttpServletResponse response);
}
