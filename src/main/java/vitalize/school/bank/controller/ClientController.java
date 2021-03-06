package vitalize.school.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;
import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.Client;
import vitalize.school.bank.entity.Task;
import vitalize.school.bank.searchform.ClientSearchForm;
import vitalize.school.bank.service.AccountService;
import vitalize.school.bank.service.ClientService;
import vitalize.school.bank.service.TaskService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/client")
public class ClientController extends BaseController {
  protected String AUTH_CODE = "CLIENT";

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
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0)Pageable pageable,
                            @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    Page<Client> clientList = clientService.getAll(pageable, searchForm);
    model.addAttribute("page", clientList);
    model.addAttribute("clientList", clientList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "client/list";
  }

  /**
   * to 顧客 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model,
                    @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    model.addAttribute("client", new Client());
    return "client/add";
  }

  /**
   * to 顧客 編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model,
                     @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    Client client = clientService.findOne(id);
    model.addAttribute("client", client);
    return "client/edit";
  }

  /**
   * to 顧客 詳細画面表示
   */
  @GetMapping(value = "{id}")
  public String view(@PathVariable Long id, Model model,
                     @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    Client client = clientService.findOne(id);
    Integer accountClientId = Integer.parseInt(String.valueOf(id));
    List<Account> accountList = accountService.findClientId(accountClientId);
    if (!accountList.isEmpty()) {
      //最新取引履歴ロジック
      List<Task> taskList = new ArrayList<Task>();
      List<Task> taskNewList = new ArrayList<Task>();
      for (Account account : accountList) {
        List<Task> taskNumber = taskService.findNumber(account.getAccountNumber());
        Task MaxTaskNumberList = taskNumber.stream().max(Comparator.comparing(tk -> tk.getId())).get();
        taskNewList.add(MaxTaskNumberList);
        for (Task task : taskNewList) {
          if (task.getType() == 0) {
            task.setStringType("新規");
          } else if(task.getType() == 1) {
            task.setStringType("入金");
          } else if (task.getType() == 2) {
            task.setStringType("出金");
          } else if (task.getType() == 3) {
            task.setStringType("振込");
          } else if (task.getType() == 4) {
            task.setStringType("振込(ATM)");
          }
          taskList.add(task);
          List<Task> taskatestList = taskList.stream().distinct().collect(Collectors.toList());
          model.addAttribute("account", accountList);
          model.addAttribute("task", taskNewList);
        }
      }
    }
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    model.addAttribute("client", client);
    return "client/view";
  }

  /**
   * to 顧客 process 登録
   */
  @PostMapping(value = "/add")
  public String create(RedirectAttributes attr, @Validated @ModelAttribute Client client, BindingResult result,
                       @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    if (result.hasErrors()) {
      return "client/add";
    }
    insertEntity(client, loginUser);
    clientService.save(client);
    Long newId = client.getId();
    attr.addFlashAttribute("message", "顧客が作成されました");
    return "redirect:/client/" + newId;
  }

  /**
   * to 顧客 process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(RedirectAttributes attr,@PathVariable Long id,@Validated @ModelAttribute Client client, BindingResult result,
                       @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    if(result.hasErrors()) return "client/edit";
    updateEntity(client, loginUser);
    clientService.save(client);
    attr.addFlashAttribute("message", "顧客が更新されました");
    return "redirect:/client/" + "{id}";
  }

  /**
   * to 顧客 削除
   */
  @PostMapping("{id}")
  public String destroy(RedirectAttributes attr,@PathVariable Long id,@ModelAttribute Client client,
                        @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    clientService.delete(id);
    Integer clientId = Math.toIntExact(client.getId());
    accountService.deleteClient(clientId);
    attr.addFlashAttribute("message", "顧客が削除されました");
    return "redirect:/client/list";
  }

}