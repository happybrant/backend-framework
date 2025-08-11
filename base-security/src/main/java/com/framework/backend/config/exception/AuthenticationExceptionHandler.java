package com.framework.backend.config.exception;

import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fucong
 * @description 捕获认证异常
 * @since 2025/08/11 15:43
 */
@ControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler {

  /**
   * 用户未登录系统异常
   *
   * @param request
   * @param response
   * @param e
   * @return
   */
  @ExceptionHandler(AuthenticationException.class)
  @ResponseBody
  public Object authorizationException(
      HttpServletRequest request, HttpServletResponse response, Exception e) {
    response.setStatus(ResultCode.UNAUTHORIZED.getCode());
    ResponseData<String> result = new ResponseData<>();
    result.setCode(ResultCode.UNAUTHORIZED.getCode());
    result.setMessage(ResultCode.UNAUTHORIZED.getMessage());
    return result;
  }

  /**
   * 用户已登录但是缺少访问权限，拦截Spring Security的权限异常
   *
   * @param request
   * @param response
   * @param accessDeniedException
   * @return
   */
  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseBody
  public Object accessDeniedException(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthorizationDeniedException accessDeniedException) {
    response.setStatus(ResultCode.FORBIDDEN.getCode());
    ResponseData<String> result = new ResponseData<>(ResultCode.FORBIDDEN);
    String str = getExceptionStackTrace(accessDeniedException);
    result.setData(str);
    return result;
  }

  private String getExceptionStackTrace(Exception exception) {
    StringWriter sw = new StringWriter();
    exception.printStackTrace(new PrintWriter(sw, true));
    String str = sw.toString();
    log.info(str);
    return str;
  }
}
