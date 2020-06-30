package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.Client;
import com.example.demo.searchform.AccountSearchForm;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private AccountService accountService;
  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  @GetMapping(value = "/list")
  /**
   * to 口座機能 一覧画面表示
   * to 口座機能 ページネーション
   * */
  public String displayList(Model model, @ModelAttribute AccountSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable) {
    Page<Account> accountList = accountService.getAll(pageable, searchForm);
    model.addAttribute("page", accountList);
    model.addAttribute("accountList", accountList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    return "account/list";
  }

  /**
   * to 口座機能 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    model.addAttribute("account", new Account());
    return "account/add";
  }
  /**
   * to 顧客詳細　→　口座機能 登録画面表示
   */
  @GetMapping(value = "/add/{id}")
  public String addClient(@PathVariable Integer id,Model model, Account account) {
    account.setClientId(id);
    model.addAttribute("account",account);
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

  /**
   * to 口座機能 process 登録
   */
  @PostMapping(value = "/add")
  public String create(@ModelAttribute Account account) {
    Integer client = account.getClientId();
    account.setId(null);
    account.setInsertUserId(9001);
    account.setUpdateUserId(9001);
    accountService.save(account);
    return "redirect:/client/"+ client;
  }

  /**
   * to 口座機能 process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(@PathVariable Long id, @ModelAttribute Account account) {
    account.setInsertUserId(9001);
    account.setUpdateUserId(9001);
    accountService.save(account);
    return "redirect:/account/"+ "{id}";
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