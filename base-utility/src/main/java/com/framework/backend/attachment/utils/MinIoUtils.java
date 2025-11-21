package com.framework.backend.attachment.utils;

import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fucong
 * @description MinIo工具类
 * @since 2025/10/10 16:40
 */
@Slf4j
@Component
public class MinIoUtils {

  @Autowired private MinioClient minioClient;

  /**
   * @param file 文件
   * @param bucketName 桶名称
   * @param filename 指定文件名称
   */
  @SneakyThrows
  public void uploadFile(MultipartFile file, String bucketName, String filename) {
    if (null == file || 0 == file.getSize()) {
      log.error("上传文件为空！");
      return;
    }
    // 判断是否存在
    createBucket(bucketName);
    minioClient.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                file.getInputStream(), file.getSize(), -1)
            .contentType(file.getContentType())
            .build());
  }

  /**
   * 通过字节流上传
   *
   * @param imageFullPath
   * @param bucketName
   * @param imageData
   * @return
   */
  @SneakyThrows
  public void uploadImage(String imageFullPath, String bucketName, byte[] imageData) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
    // 判断是否存在
    createBucket(bucketName);
    minioClient.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(imageFullPath).stream(
                byteArrayInputStream, byteArrayInputStream.available(), -1)
            .contentType(".jpg")
            .build());
  }

  /**
   * 删除文件
   *
   * @param bucketName
   * @param fileName
   * @return
   */
  @SneakyThrows
  public void removeFile(String bucketName, String fileName) {
    // 判断桶是否存在
    boolean res = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (res) {
      // 删除文件
      minioClient.removeObject(
          RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }
  }

  /**
   * 下载文件
   *
   * @param fileName
   * @param bucketName
   * @param response
   */
  @SneakyThrows
  public void fileDownload(String fileName, String bucketName, HttpServletResponse response) {

    InputStream inputStream;
    OutputStream outputStream;

    if (StringUtils.isBlank(fileName)) {
      response.setHeader("Content-type", "text/html;charset=UTF-8");
      String data = "文件下载失败";
      OutputStream ps = response.getOutputStream();
      ps.write(data.getBytes(StandardCharsets.UTF_8));
      return;
    }
    outputStream = response.getOutputStream();
    // 获取文件对象
    inputStream =
        minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
    byte[] buf = new byte[1024];
    int length;
    response.reset();
    response.setHeader(
        "Content-Disposition",
        "attachment;filename="
            + URLEncoder.encode(
                fileName.substring(fileName.lastIndexOf("/") + 1), StandardCharsets.UTF_8));
    response.setContentType("application/octet-stream");
    response.setCharacterEncoding("UTF-8");
    // 输出文件
    while ((length = inputStream.read(buf)) > 0) {
      outputStream.write(buf, 0, length);
    }
    inputStream.close();
    outputStream.close();
  }

  /**
   * 获取文件url
   *
   * @param fileName
   * @return
   */
  @SneakyThrows
  public String getFileUrl(String fileName, String bucketName) {

    // 自定义响应头：让浏览器预览而非下载
    Map<String, String> headers = new HashMap<>();
    // "inline" 表示在浏览器中直接显示（而非下载）
    headers.put(
        "response-content-disposition",
        "inline; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    return minioClient.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
            // 存储桶名称
            .bucket(bucketName)
            // 要访问的文件名称
            .object(fileName)
            // 必须指定HTTP方法（GET表示读取）
            .method(Method.GET)
            .extraQueryParams(headers)
            .build());
  }

  @SneakyThrows
  public void createBucket(String bucketName) {
    if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
  }
}
