package com.framework.backend.attachment.service.impl;

import com.framework.backend.attachment.entity.Attachment;
import com.framework.backend.attachment.service.AttachmentService;
import com.framework.backend.attachment.service.FileOperator;
import com.framework.backend.attachment.utils.MinIoUtils;
import com.framework.backend.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fucong
 * @description To do
 * @since 2025/10/11 15:03
 */
@Service
@Slf4j
public class MinIoFileOperator implements FileOperator {

  @Autowired private MinIoUtils minioUtils;
  @Autowired private AttachmentService attachmentService;

  @Value("${minio.endpoint}")
  private String endpoint;

  @Value("${minio.bucketName}")
  private String bucketName;

  final String storageMode = "minio";

  @Override
  public String upload(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    if (StringUtils.isEmpty(originalFilename)) {
      throw new BusinessException("文件名称不存在！");
    }
    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    String newFileName = UUID.randomUUID() + extension;
    minioUtils.uploadFile(file, bucketName, newFileName);
    String path = endpoint + "/" + bucketName + "/" + newFileName;
    Attachment attachment = new Attachment();
    attachment.setName(newFileName);
    attachment.setOriginName(originalFilename);
    attachment.setFileSize(file.getSize());
    attachment.setStorageMode(storageMode);
    attachment.setFullPath(path);
    attachmentService.save(attachment);
    return attachment.getId();
  }

  @Override
  public void download(String attachmentId, HttpServletResponse response) {
    if (StringUtils.isEmpty(attachmentId)) {
      throw new BusinessException("附件 ID 不能为空");
    }

    // 根据 attachmentId 查询附件信息
    Attachment attachment = attachmentService.getById(attachmentId);
    if (null == attachment) {
      throw new BusinessException("附件不存在");
    }

    // 从完整路径中提取文件名
    String fullPath = attachment.getFullPath();
    if (StringUtils.isEmpty(fullPath)) {
      throw new BusinessException("附件路径不存在");
    }

    // 从路径中提取对象名称 (endpoint/bucketName/objectName 格式)
    String objectName;
    if (fullPath.contains(bucketName + "/")) {
      objectName = fullPath.substring(fullPath.indexOf(bucketName + "/") + bucketName.length() + 1);
    } else {
      // 如果路径格式不匹配，尝试直接获取最后一段
      objectName = fullPath.substring(fullPath.lastIndexOf("/") + 1);
    }

    // 设置响应头，使用原文件名
    try {
      response.reset();
      response.setHeader(
          "Content-Disposition",
          "attachment;filename="
              + URLEncoder.encode(attachment.getOriginName(), StandardCharsets.UTF_8));
      response.setContentType("application/octet-stream");
      response.setCharacterEncoding("UTF-8");

      // 调用 MinIoUtils 下载文件
      minioUtils.fileDownload(objectName, bucketName, response);
    } catch (Exception e) {
      log.error("下载文件失败：{}", e.getMessage());
      throw new BusinessException("下载文件失败：" + e.getMessage());
    }
  }
}
