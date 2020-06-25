package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private AccountService accountService;
  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  @GetMapping(value = "/list")
  /** to 口座機能 一覧画面表示*/
  /** to 口座機能 ページネーション*/
  public String displayList(Model model, @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable) {
    Page<Account> accountlist = accountService.getAll(pageable);
    model.addAttribute("page", accountlist);
    model.addAttribute("accountlist", accountlist.getContent());
    model.addAttribute("url", "list");
    return "account/list";
  }

  /**
   * to 口座機能 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    return "account/add";
  }

  /**
   * to 口座機能 編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    Account account = accountService.findOne(id);
    model.addAttribute("account", account);
    return "account/edit";
  }

  /**
   * to 口座機能 詳細画面表示
   */
  @GetMapping(value = "{id}")
  public String view(@PathVariable Long id, Model model) {
    Account account = accountService.findOne(id);
    model.addAttribute("account", account);
    return "account/view";
  }

  @PostMapping(value = "/search")
  public ModelAndView login(ModelAndView mav
    , @RequestParam("accountNumber") String accountNumber, @RequestParam("clientId") String clientId
    , @RequestParam("branchCode") String branchCode) {
    mav.addObject("accountNumber", accountNumber);
    mav.addObject("clientId", clientId);
    mav.addObject("branchCode", branchCode);
    List<Account> accountlist = accountService.search(accountNumber, clientId, branchCode);
    mav.addObject("accountlist", accountlist);
    return mav;
  }

  /**
   * to 口座機能 process 登録
   */
  @PostMapping(value = "/add")
  public String create(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) return "account/add";
    account.setInsertUserId(9001);
    account.setUpdateUserId(9001);
    accountService.save(account);
    return "redirect:/account/list";
  }

  /**
   * to 口座機能 process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(@PathVariable Long id, @ModelAttribute Account account) {
    account.setInsertUserId(9001);
    account.setUpdateUserId(9001);
    account.setId(id);
    accountService.save(account);
    return "redirect:/account/list";
  }

  /**
   * to 口座機能 削除
   */
  @PostMapping("{id}")
  public String destroy(@PathVariable Long id) {
    accountService.delete(id);
    return "redirect:/account/list";
  }

}