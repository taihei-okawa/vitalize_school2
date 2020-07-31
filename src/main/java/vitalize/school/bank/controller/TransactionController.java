package vitalize.school.bank.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;
import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.Task;
import vitalize.school.bank.entity.Transaction;
import vitalize.school.bank.searchform.TransactionSearchForm;
import vitalize.school.bank.service.AccountService;
import vitalize.school.bank.service.MstFeeService;
import vitalize.school.bank.service.TaskService;
import vitalize.school.bank.service.TransactionService;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/transaction")
public class TransactionController extends BaseController {
  /**
   * 取引履歴機能情報 Service
   */
  @Autowired
  private TransactionService transactionService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private MstFeeService mstFeeService;

  protected String AUTH_CODE = "TRANSACTION";

  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  @GetMapping(value = "/list")
  /** to 取引履歴 一覧画面表示 ページネーション*/
  public String displayList(Model model, @ModelAttribute TransactionSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    Page<Transaction> transactionlist = transactionService.getAll(pageable, searchForm);

    for(Transaction transaction:transactionlist) {
      if (transaction.getType() == 0) {
        transaction.setStringType("新規");
      } else if(transaction.getType() == 1) {
        transaction.setStringType("入金");
      } else if (transaction.getType() == 2) {
        transaction.setStringType("出金");
      } else if (transaction.getType() == 3) {
        transaction.setStringType("振込");
      } else if (transaction.getType() == 4) {
        transaction.setStringType("振込(ATM)");
      }
    }
    model.addAttribute("page", transactionlist);
    model.addAttribute("transactionlist", transactionlist.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "transaction/list";
  }
  /**
   * to 取引履歴 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model,
                    @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    List<Account> account = accountService.findAll();
    List<Integer> accountList = new ArrayList<Integer>();
    //昇順にソート
    for (Account accountDate:account){
      accountList.add(accountDate.getAccountNumber());
    }
    Collections.sort(accountList);
    model.addAttribute("accountList", accountList);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "transaction/add";
  }

  /**
   * to 取引履歴 process 登録
   */
  @Transactional
  @PostMapping(value = "/add")
  public String create(RedirectAttributes attr, @ModelAttribute Transaction transaction,
    @AuthenticationPrincipal LoginUser loginUser) throws AuthException, ParseException {
      checkAuth(loginUser, AUTH_CODE);
      insertEntity(transaction, loginUser);

    //取引額バリデーション
    if (transaction.getType() == 2 || transaction.getType() == 3) {
      List<Task> accountNumberList = taskService.findOne(transaction.getAccountNumber());
      Task MaxTaskPayList = accountNumberList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      if(transaction.getAmount() > MaxTaskPayList.getBalance()){
        attr.addFlashAttribute("message", "取引額が対象の出金口座の残高を上回っています");
        return "redirect:/transaction/add";
      }
    }
    transactionService.AccountPay(transaction);
    if(transaction.getPoolFlag()==1){
      attr.addFlashAttribute("message", "取引履歴が作成されました(指定した日時まで反映されません)");
    }else{
      attr.addFlashAttribute("message", "取引履歴が作成されました(反映されるまで少々お待ちください)");
    }
    return "redirect:/transaction/list";
  }

  /** to CSVダウンロード*/
  @ResponseBody
  @RequestMapping(value = "/download/csv", method = RequestMethod.GET)
  public Object downloadCsv(@ModelAttribute TransactionSearchForm searchForm,
                            @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    CsvMapper csvMapper = new CsvMapper();
    CsvSchema schema = csvMapper.schemaFor(Transaction.class).withHeader();
    // ↓DBからデータをセレクト
    List<Transaction> dataList = transactionService.searchAll(searchForm);
    String csv = null;
    try {
      csv = csvMapper.writer(schema).writeValueAsString(dataList);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "text/csv;");
    String  filename = "transaction";
    headers.setContentDispositionFormData("filename", filename + ".csv");
    return new ResponseEntity<>(csv.getBytes(), headers, HttpStatus.OK);
  }

}