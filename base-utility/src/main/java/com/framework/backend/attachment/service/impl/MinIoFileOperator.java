package com.framework.backend.attachment.service.impl;

import com.framework.backend.attachment.entity.Attachment;
import com.framework.backend.attachment.service.AttachmentService;
import com.framework.backend.attachment.service.FileOperator;
import com.framework.backend.attachment.utils.MinIoUtils;
import com.framework.backend.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fucong
 * @description To do
 * @since 2025/10/11 15:03
 */
public class MinIoFileOperator implements FileOperator {

  @Autowired private MinIoUtils minioUtils;
  @Autowired private AttachmentService attachmentService;

  @Value("${minio.endpoint}")
  private String endpoint;

  @Override
  public void upload(MultipartFile file, String location) {
    String originalFilename = file.getOriginalFilename();
    if (StringUtils.isEmpty(originalFilename)) {
      throw new BusinessException("文件名称不存在！");
    }
    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    String newFileName = UUID.randomUUID() + extension;
    minioUtils.uploadFile(file, location, newFileName);
    String path = endpoint + "/" + location + "/" + newFileName;
    Attachment attachment = new Attachment();
    attachment.setName(newFileName);
    attachment.setOriginName(originalFilename);
    attachment.setFileSize(file.getSize());
    attachment.setLocation(location);
    attachment.setStorageMode("minio");
    attachment.setFullPath(path);
    attachmentService.save(attachment);
  }

  @Override
  public void download(String attachmentId, HttpServletResponse response) {}
}
