package com.example.demo.controller;

import com.example.demo.entity.MstUser;
import com.example.demo.service.MstUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import java.util.List;

@Controller
@RequestMapping("/mst_user")
public class MstUserController {

  @Autowired
  private MstUserService mstUserService;

  /**
   * to 検索機能　社員一覧画面
   */
//  @GetMapping("/list")
//  public String search() {
//    return "/list";
//  }
  @RequestMapping(value = "/mst_user/list", method = POST)
  public String displayList(Model model
          , @RequestParam(name = "id", required = false) Long id
          , @RequestParam(name = "userName", required = false) String userName
          , @RequestParam(name = "branchCode", required = false) Integer branchCode
  ) {
    List<MstUser> userList = mstUserService.findUsers(id, userName, branchCode);
    model.addAttribute("mstUserlist", userList);
    model.addAttribute("mstUserlistSize", userList.size());
    return "mst_user/list";
  }


  /**
   * to 社員 一覧画面表示
   */
  @GetMapping(value = "/list")
  public String displayList(Model model) {
    List<MstUser> userList = mstUserService.findAll();
    model.addAttribute("mstUserlist", userList);
    return "mst_user/list";
  }

  /**
   * to 社員 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    return "mst_user/add";
  }

  /**
   * to 社員 詳細画面表示
   */
  @GetMapping(value = "{id}")
  public String view(@PathVariable Long id, Model model) {
    MstUser mstUser = mstUserService.findOne(id);
    model.addAttribute("mstUser", mstUser);
    return "mst_user/view";
  }

  /**
   * to ユーザー process 登録
   */
  @PostMapping(value = "/add")
  public String create(@ModelAttribute MstUser mstUser) {
    mstUser.setInsertUserId(9001);
    mstUser.setUpdateUserId(9001);
    mstUser.setStatus(1);
    mstUserService.save(mstUser);
    return "redirect:/mst_user/list";
  }

  /**
   * to 社員　更新、編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    MstUser mstUser = mstUserService.findOne(id);
    model.addAttribute("mstUser", mstUser);
    model.addAttribute("id", id);
    return "mst_user/edit";
  }

  /**
   * to 社員 process 編集
   */
  @PostMapping(value = "/edit/{id}") //PostMappingを使う
  public String update(@PathVariable Long id, @ModelAttribute MstUser mstUser) {
    mstUser.setInsertUserId(9001);
    mstUser.setUpdateUserId(9001);
    mstUser.setStatus(1);
    mstUserService.save(mstUser);
    return "redirect:/mst_user/list";
  }

  /**
   * to 削除機能　社員一覧画面
   */
  @PostMapping("{id}")
  public String destroy(@PathVariable Long id) {
    mstUserService.delete(id);
    return "redirect:/mst_user/list";
  }

}