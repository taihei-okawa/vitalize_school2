package vitalize.school.bank.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import java.text.SimpleDateFormat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.MstFee;
import vitalize.school.bank.searchform.TransactionSearchForm;

import vitalize.school.bank.entity.Task;
import vitalize.school.bank.entity.Transaction;
import vitalize.school.bank.service.AccountService;
import vitalize.school.bank.service.MstFeeService;
import vitalize.school.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vitalize.school.bank.service.TaskService;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;


@Controller
@RequestMapping("/transaction")
public class TransactionController {
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

  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  @GetMapping(value = "/list")
  /** to 取引履歴 一覧画面表示 ページネーション*/
  public String displayList(Model model, @ModelAttribute TransactionSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable) {
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
  public String add(Model model) {
    List<Account> account = accountService.findAll();
    List<Integer> accountList = new ArrayList<Integer>();
    //昇順にソート
    for (Account accountDate:account){
      accountList.add(accountDate.getAccountNumber());
    }
    Collections.sort(accountList);
    model.addAttribute("accountList", accountList);
    return "transaction/add";
  }

  /**
   * to 取引履歴 process 登録
   */
  @Transactional
  @PostMapping(value = "/add")
  public String create(RedirectAttributes attr, @ModelAttribute Transaction transaction) throws ParseException {
    transactionService.AccountPay(transaction);
    attr.addFlashAttribute("message", "※取引履歴が作成されました(反映されるまでお待ちください)※");
    return "redirect:/transaction/list";
  }

  /** to CSVダウンロード*/
  @ResponseBody
  @RequestMapping(value = "/download/csv", method = RequestMethod.GET)
  public Object downloadCsv(@ModelAttribute TransactionSearchForm searchForm) {
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