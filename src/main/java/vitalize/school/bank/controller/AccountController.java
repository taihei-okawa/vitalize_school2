package vitalize.school.bank.controller;

import org.springframework.validation.annotation.Validated;
import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.Task;
import vitalize.school.bank.searchform.AccountSearchForm;
import vitalize.school.bank.service.AccountService;
import vitalize.school.bank.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalize.school.bank.service.TransactionService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private AccountService accountService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private TransactionService transactionService;

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
    List<Task> TaskList = taskService.findNumber(account.getAccountNumber());
    if (TaskList == null || TaskList.size() == 0) {
      model.addAttribute("account", account);
      model.addAttribute("task", new Task());
    }else {
      Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      model.addAttribute("account", account);
      model.addAttribute("task", MaxTaskList);
    }
    return "account/view";
  }

  /**
   * to 口座機能 process 登録
   */
  @PostMapping(value = "/add")
  public String create(@ModelAttribute Account account) {
    Integer client = account.getClientId();
    account.setId(null);
    List<Account> accountList = accountService.findAll();
    if(accountList.isEmpty()){
      account.setAccountNumber(100000);
    }else{
      Account MaxAccountList = accountList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      Integer MaxAccountNumber = MaxAccountList.getAccountNumber();
      String MaxNumber = String.format("%06d", MaxAccountNumber + 1);
      account.setAccountNumber(Integer.parseInt(MaxNumber));
    }
    account.setInsertUserId(9001);
    account.setUpdateUserId(9001);
    accountService.save(account);
    return "redirect:/client/" + client;
  }

  /**
   * to 口座機能 削除
   */
  @PostMapping("{id}")
  public String destroy(@PathVariable Long id) {
    Account account = accountService.findOne(id);
    Integer accountNumber = account.getAccountNumber();
    taskService.delete(accountNumber);
    transactionService.delete(accountNumber);
    accountService.delete(id);
    return "redirect:/account/list";
  }

}