package com.framework.backend.dict.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.common.MyPage;
import com.framework.backend.dict.entity.Dict;
import java.util.List;

/**
 * @author fucong
 * @description 字典服务接口
 * @since 2025/9/15 16:29
 */
public interface DictService extends IService<Dict> {
  /**
   * 添加字典
   *
   * @param dict
   */
  void addDict(Dict dict);

  /**
   * 修改字典
   *
   * @param dict
   */
  void updateDict(Dict dict);

  /**
   * 删除字典
   *
   * @param idList
   */
  void deleteDict(List<String> idList);

  /**
   * 分页查询字典
   *
   * @param dict
   * @return
   */
  MyPage<Dict> pageList(Dict dict);

  /**
   * 获取字典集信息
   *
   * @param groupCode
   * @return
   */
  List<Dict> listByGroupCode(String groupCode);

  /**
   * @param code
   * @return
   */
  List<Tree<String>> listTree(String code);
}
