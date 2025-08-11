package com.framework.backend.common.exception;

import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fucong
 * @description 全局异常处置
 * @since 2025/08/11 15:43
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 系统中的自定义业务异常
   *
   * @param request
   * @param response
   * @param businessException
   * @return
   */
  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  public Object businessException(
      HttpServletRequest request,
      HttpServletResponse response,
      BusinessException businessException) {
    // 设置HTTP状态码
    response.setStatus(ResultCode.SUCCESS.getCode());
    // 当ResponseResultCode存在时按它返回异常信息
    ResultCode resultCode = businessException.getResultCode();
    if (resultCode != null) {
      return ResponseData.failed(resultCode);
    }
    // 当ResponseResultCode不存在时就按message异常信息
    else {
      return ResponseData.failed(businessException.getMessage());
    }
  }

  /**
   * 系统中的所有Exception异常
   *
   * @param request
   * @param response
   * @param exception
   * @return
   */
  @ExceptionHandler(Exception.class)
  @ResponseBody
  public Object commonException(
      HttpServletRequest request, HttpServletResponse response, Exception exception) {
    response.setStatus(ResultCode.FAILED.getCode());
    ResponseData<String> result = new ResponseData<>();
    result.setCode(ResultCode.FAILED.getCode());
    result.setMessage("系统异常");
    String str = getExceptionStackTrace(exception);
    result.setData(str);
    return result;
  }

  private String getExceptionStackTrace(Exception exception) {
    StringWriter sw = new StringWriter();
    exception.printStackTrace(new PrintWriter(sw, true));
    String str = sw.toString();
    // logger.error(str);
    return str;
  }
}
