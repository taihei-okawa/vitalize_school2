package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.searchform.ClientSearchForm;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.ClientService;
import com.example.demo.entity.Client;
import com.example.demo.service.AccountService;
import com.example.demo.entity.Account;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

  @Autowired
  private ClientService clientService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private TaskService taskService;
  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  /**
   * to 顧客 一覧画面表示
   * to 顧客 ページネーション
   */
  @GetMapping(value = "/list")
  public String displayList(Model model, @ModelAttribute ClientSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0)Pageable pageable) {
    Page<Client> clientList = clientService.getAll(pageable, searchForm);
    model.addAttribute("page", clientList);
    model.addAttribute("clientList", clientList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    return "client/list";
  }

  /**
   * to 顧客 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    model.addAttribute("client", new Client());
    return "client/add";
  }

  /**
   * to 顧客 編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    Client client = clientService.findOne(id);
    model.addAttribute("client", client);
    return "client/edit";
  }

  /**
   * to 顧客 詳細画面表示
   */
  @GetMapping(value = "{id}")
  public String view(@PathVariable Long id, Model model) {
    Client client = clientService.findOne(id);
    Integer accountClientId = Integer.parseInt(String.valueOf(id));
    List<Account> account = accountService.findClientId(accountClientId);
    Integer accountNumber = account.get(0).getAccountNumber();
    List<Task> task = taskService.findNumber(accountNumber);
    model.addAttribute("client", client);
    model.addAttribute("account", account);
    model.addAttribute("task", task);
    return "client/view";
  }

  /**
   * to 顧客 process 登録
   */
  @PostMapping(value = "/add")
  public String create(@ModelAttribute Client client) {
    client.setInsertUserId(9001);
    client.setUpdateUserId(9001);
    clientService.save(client);
    Long newId = client.getId();
    return "redirect:/client/" + newId;
  }

  /**
   * to 顧客 process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(@PathVariable Long id, @ModelAttribute Client client) {
    client.setInsertUserId(9001);
    client.setUpdateUserId(9001);
    clientService.save(client);
    return "redirect:/client/" + "{id}";
  }

  /**
   * to 顧客 削除
   */
  @PostMapping("{id}")
  public String destroy(@PathVariable Long id) {
    clientService.delete(id);
    return "redirect:/client/list";
  }

}