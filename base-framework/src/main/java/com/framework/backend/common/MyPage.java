package com.framework.backend.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/**
 * @author fucong
 * @description 自定义分页
 * @since 2025/08/11 15:45
 */
@Getter
@JsonIgnoreProperties({"size", "current"})
public class MyPage<T> extends Page<T> {
  /** 每页显示记录数量 */
  protected long pageSize;

  /** 当前页号 */
  protected long currentPage;

  public MyPage() {}

  public MyPage(long currentPage, long pageSize) {
    super(currentPage, pageSize);
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  public MyPage(long currentPage, long pageSize, long total) {
    super(currentPage, pageSize, total);
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  public MyPage(long currentPage, long pageSize, boolean isSearchCount) {
    super(currentPage, pageSize, isSearchCount);
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  public MyPage(long currentPage, long pageSize, long total, boolean isSearchCount) {
    super(currentPage, pageSize, total, isSearchCount);
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  public void setPageSize(long pageSize) {
    this.pageSize = pageSize;
    this.size = pageSize;
  }

  public void setCurrentPage(long currentPage) {
    this.currentPage = currentPage;
    this.current = currentPage;
  }
}
