package com.example.demo.controller;

import com.example.demo.searchform.MstFeeSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.MstFeeService;
import com.example.demo.entity.MstFee;
import java.util.List;

@Controller
@RequestMapping("/mst_fee")
public class MstFeeController {

  @Autowired
  private MstFeeService mstFeeService;

  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  /**
   * to 手数料マスタ 一覧画面表示
   * to 手数料マスタ ページネーション
   */
  @GetMapping(value = "/list")
  public String displayList(Model model, @ModelAttribute MstFeeSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0)Pageable pageable) {
    Page<MstFee> mstFeeList = mstFeeService.getAll(pageable, searchForm);
    model.addAttribute("page", mstFeeList);
    model.addAttribute("mstFeeList", mstFeeList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    return "mst_fee/list";
  }

  /**
   * to 手数料マスタ 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    model.addAttribute("mstFee", new MstFee());
    return "mst_fee/add";
  }

  /**
   * to 手数料マスタ 編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    MstFee mstFee = mstFeeService.findOne(id);
    model.addAttribute("mstFee", mstFee);
    return "mst_fee/edit";
  }

  /**
   * to 手数料マスタ 詳細画面表示
   */
  @GetMapping(value = "{id}")
  public String view(@PathVariable Long id, Model model) {
    MstFee mstFee = mstFeeService.findOne(id);
    model.addAttribute("mstFee", mstFee);
    return "mst_fee/view";
  }

  /**
   * to 手数料マスタ process 登録
   */
  @PostMapping(value = "/add")
  public String create(@ModelAttribute MstFee mstFee) {
    mstFee.setInsertUserId(9001);
    mstFee.setUpdateUserId(9001);
    mstFeeService.save(mstFee);
    Long newId = mstFee.getId();
    return "redirect:/mst_fee/" + newId;
  }

  /**
   * to 手数料マスタ process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(@PathVariable Long id, @ModelAttribute MstFee mstFee) {
    mstFee.setInsertUserId(9001);
    mstFee.setUpdateUserId(9001);
    mstFeeService.save(mstFee);
    return "redirect:/mst_fee/" + "{id}";
  }

  /**
   * to 手数料マスタ 削除
   */
  @PostMapping("{id}")
  public String destroy(@PathVariable Long id) {
    mstFeeService.delete(id);
    return "redirect:/mst_fee/list";
  }

}