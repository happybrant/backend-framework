package com.framework.backend.dict.controller;

import cn.hutool.core.lang.tree.Tree;
import com.framework.backend.common.MyPage;
import com.framework.backend.common.result.ResponseResult;
import com.framework.backend.dict.entity.Dict;
import com.framework.backend.dict.service.DictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fucong
 * @description To do
 * @since 2025/9/15 16:27
 */
@RestController
@ResponseResult
@RequestMapping("/utility/dict")
@Tag(name = "DictController", description = "字典管理(0104)")
public class DictController {
  @Autowired DictService dictService;

  @PostMapping("/add")
  public void addDict(@RequestBody @Valid Dict dict) {
    dictService.addDict(dict);
  }

  @PostMapping("/update")
  public void updateDict(@RequestBody Dict dict) {
    dictService.updateDict(dict);
  }

  @PostMapping("/delete")
  public void deleteDict(@RequestBody List<String> idList) {
    dictService.deleteDict(idList);
  }

  @PostMapping("/pageList")
  public MyPage<Dict> pageList(@RequestBody Dict dict) {
    return dictService.pageList(dict);
  }

  @GetMapping("getOne/{id}")
  public Dict getOneById(@PathVariable String id) {
    return dictService.getById(id);
  }

  @GetMapping("listByCode/{code}")
  public List<Tree<String>> listByCode(@PathVariable String code) {
    return dictService.listTree(code);
  }
}
