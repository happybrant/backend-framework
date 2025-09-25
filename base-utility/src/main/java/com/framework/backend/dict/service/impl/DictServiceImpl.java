package com.framework.backend.dict.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.common.MyPage;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.dict.dao.DictMapper;
import com.framework.backend.dict.entity.Dict;
import com.framework.backend.dict.service.DictService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description To do
 * @since 2025/9/15 16:29
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
  @Override
  public void addDict(Dict dict) {
    // 同一分组下字典项编码不能重复
    QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Dict::getGroupCode, dict.getGroupCode());
    queryWrapper.lambda().eq(Dict::getCode, dict.getCode());
    long count = count(queryWrapper);
    if (count > 0) {
      throw new BusinessException("同一分组下字典项编码不能重复！");
    }
    save(dict);
  }

  @Override
  public void updateDict(Dict dict) {
    if (StringUtils.isBlank(dict.getId())) {
      throw new BusinessException("字典id不能为空");
    }
    QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Dict::getGroupCode, dict.getGroupCode());
    queryWrapper.lambda().eq(Dict::getCode, dict.getCode());
    Dict exist = getOne(queryWrapper, false);
    if (exist != null && !exist.getId().equals(dict.getId())) {
      throw new BusinessException("同一分组下字典项编码不能重复！");
    }
    updateById(dict);
  }

  @Override
  public void deleteDict(List<String> idList) {
    if (idList.isEmpty()) {
      throw new BusinessException("字典id集合不能为空");
    }
    removeBatchByIds(idList);
  }

  @Override
  public MyPage<Dict> pageList(Dict dict) {
    QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .lambda()
        .like(StringUtils.isNotBlank(dict.getName()), Dict::getName, dict.getName());
    queryWrapper
        .lambda()
        .like(StringUtils.isNotBlank(dict.getCode()), Dict::getCode, dict.getCode());
    queryWrapper
        .lambda()
        .eq(StringUtils.isNotBlank(dict.getStatus()), Dict::getStatus, dict.getStatus());
    queryWrapper
        .lambda()
        .eq(StringUtils.isNotBlank(dict.getGroupCode()), Dict::getGroupCode, dict.getGroupCode());
    queryWrapper
        .lambda()
        .eq(StringUtils.isNotBlank(dict.getParentId()), Dict::getParentId, dict.getParentId());
    queryWrapper.lambda().orderByAsc(Dict::getSort);
    MyPage<Dict> page = new MyPage<>(dict.getCurrentPage(), dict.getPageSize());
    page = baseMapper.selectPage(page, queryWrapper);
    return page;
  }

  @Override
  public List<Dict> listByGroupCode(String groupCode) {
    QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().ne(Dict::getParentId, "0");
    queryWrapper.lambda().eq(Dict::getGroupCode, groupCode);
    queryWrapper.lambda().orderByAsc(Dict::getSort);
    // 只查询“启用”状态的字典项
    queryWrapper.lambda().eq(Dict::getStatus, "active");
    return baseMapper.selectList(queryWrapper);
  }

  @Override
  public List<Tree<String>> listTree(String code) {
    QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Dict::getGroupCode, code);
    queryWrapper.lambda().orderByAsc(Dict::getStatus);
    List<Dict> list = list(queryWrapper);
    TreeNodeConfig config = new TreeNodeConfig();
    // 默认为id可以不设置
    config.setIdKey("id");
    // 默认为parentId可以不设置
    config.setParentIdKey("parentId");
    // 排序字段
    config.setWeightKey("sort");
    // 3.转树，Tree<>里面泛型为id的类型
    List<Tree<String>> treeNodes =
        TreeUtil.build(
            list,
            "0",
            config,
            (treeNode, tree) -> {
              // 基本属性
              tree.setId(treeNode.getId());
              tree.setParentId(treeNode.getParentId());
              tree.setWeight(treeNode.getSort());
              tree.setName(treeNode.getName());
              // 扩展属性
              tree.putExtra("sort", treeNode.getSort());
              tree.putExtra("status", treeNode.getStatus());
              tree.putExtra("createUser", treeNode.getCreateUser());
              tree.putExtra("createTime", treeNode.getCreateTime());
              tree.putExtra("updateUser", treeNode.getUpdateUser());
              tree.putExtra("updateTime", treeNode.getUpdateTime());
            });
    //    logger.info("将源数据转化为树：" + JSONUtil.toJsonStr(treeNodes));
    return treeNodes;
  }
}
