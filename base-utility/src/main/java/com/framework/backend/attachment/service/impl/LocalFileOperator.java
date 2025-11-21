package com.framework.backend.attachment.service.impl;

import com.framework.backend.attachment.entity.Attachment;
import com.framework.backend.attachment.service.AttachmentService;
import com.framework.backend.attachment.service.FileOperator;
import com.framework.backend.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fucong
 * @description To do
 * @since 2025/10/10 14:41
 */
@Service
public class LocalFileOperator implements FileOperator {
  @Value("${base.utility.file.dir}")
  private String dir;

  @Autowired private AttachmentService attachmentService;

  final String storageMode = "local";

  @Override
  public void upload(MultipartFile file, String location) {
    String originalFilename = file.getOriginalFilename();
    if (StringUtils.isEmpty(originalFilename)) {
      throw new BusinessException("文件名称不存在！");
    }
    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    String newFileName = UUID.randomUUID() + extension;
    String directory = dir + "/" + location;
    if (!createDirectory(directory)) {
      throw new BusinessException("目录创建失败！");
    }
    String path = dir + location + "/" + newFileName;
    try {
      file.transferTo(new File(path));
    } catch (IOException e) {
      throw new BusinessException(e.getMessage());
    }
    Attachment attachment = new Attachment();
    attachment.setName(newFileName);
    attachment.setOriginName(originalFilename);
    attachment.setFileSize(file.getSize());
    attachment.setLocation(directory);
    attachment.setStorageMode(storageMode);
    attachment.setFullPath(path);
    attachmentService.save(attachment);
  }

  @Override
  public void download(String attachmentId, HttpServletResponse response) {
    // 根据id查找文件
    Attachment attachment = attachmentService.getById(attachmentId);
    if (attachment != null) {
      String fullPath = attachment.getFullPath();
      File file = new File(fullPath);
      // 将文件写入输入流
      try {
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream fis = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        // Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        // attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader(
            "Content-Disposition",
            "attachment;filename="
                + URLEncoder.encode(attachment.getOriginName(), StandardCharsets.UTF_8));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
      } catch (IOException e) {
        throw new BusinessException(e.getMessage());
      }
    }
  }

  /**
   * 创建目录
   *
   * @param path
   * @return
   */
  public boolean createDirectory(String path) {
    File directory = new File(path);
    if (!directory.exists()) {
      return directory.mkdir();
    }
    return true;
  }
}
