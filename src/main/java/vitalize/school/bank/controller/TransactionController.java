package vitalize.school.bank.controller;

import java.text.ParsePosition;
import java.util.*;
import java.text.SimpleDateFormat;

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
    model.addAttribute("page", transactionlist);
    model.addAttribute("transactionlist", transactionlist.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);

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
  public String create( @ModelAttribute Transaction transaction) {
    /** to 本日日付に代入*/
    if (transaction.getStringTradingDate().isEmpty()){
      Date date = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
      String strDate = dateFormat.format(date);
      transaction.setStringTradingDate(strDate);
    }

    /** to 取引履歴の最新の情報だけを取得 */
    List<Task> TaskList = taskService.findNumber(transaction.getAccountNumber());
    Integer amount = transaction.getAmount();
    Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
    Integer balance = MaxTaskList.getBalance();

//    /** to 手数料計算 */
//    //口座テーブルに口座番号で支店名を検索
//    Integer accountNumber = transaction.getAccountNumber();
//    List<Account> Account  = accountService.findAccount(accountNumber);
//    //手数料テーブルに支店名で検索
//    String branchCode = Account.get(0).getBranchCode();
//    List<MstFee> mstFeeList = mstFeeService.findBranchCode(branchCode);
//    //曜日判断
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    String strTradingDate = dateFormat.format(transaction.getStringTradingDate());
//    mstFeeList.stream()
//      .filter(msl -> msl.getBusinessDay() == strTradingDate || msl.getHoliday() == strTradingDate)
//      .collect(Collectors.toList());
//    //時間判断
//    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
//    Integer intTradingTime = Integer.valueOf(timeFormat.format(transaction.getStringTradingDate()));
//    mstFeeList.stream()
//      .filter(msl -> Integer.valueOf(msl.getStartDay()) <= intTradingTime && Integer.valueOf(msl.getEndDay()) <= intTradingTime)
//      .collect(Collectors.toList());
//    //手数料決定
//    Integer feePrice = mstFeeList.get(0).getFeePrice();

    /** to 日付型に変換*/
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
    Date date = sdf.parse(transaction.getStringTradingDate(), new ParsePosition(0));
    transaction.setTradingDate(date);
    transaction.setInsertUserId(9001);
    transaction.setUpdateUserId(9001);

    /**
     * to 処理　判断 取引履歴 process 登録
     */
    //入金
    if (transaction.getType() == 1){
      transaction.setPayAccountNumber(transaction.getAccountNumber());
      Integer answer;
      answer = balance + amount;
      transaction.setBalance(answer);
    }
    //出金
    if (transaction.getType() == 2) {
      transaction.setPayAccountNumber(transaction.getAccountNumber());
      Integer answer;
      answer = balance - amount;
      transaction.setBalance(answer);
    }
    //振込
    if (transaction.getType() == 3) {
      /** to 自分の口座　出金処理 */
      Integer answer;
      answer = balance - amount;
      transaction.setBalance(answer);
      List<Transaction> transactionList = new ArrayList<Transaction>();
      transactionList.add(0,transaction);

      /** to 相手の口座　入金処理 */
      List<Task> TaskPayList = taskService.findPayNumber(transaction.getPayAccountNumber());
      Task MaxTaskPayList = TaskPayList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      Integer payBalance = MaxTaskPayList.getBalance();
      Integer payAnswer;
      payAnswer = payBalance + amount;
      Transaction transactionNew = new Transaction();
      transactionNew.setAccountNumber(transaction.getPayAccountNumber());
      transactionNew.setPayAccountNumber(transaction.getAccountNumber());
      transactionNew.setPoolFlag(transaction.getPoolFlag());
      transactionNew.setAmount(transaction.getAmount());
      transactionNew.setBalance(payAnswer);
      transactionNew.setType(transaction.getType());
      transactionNew.setInsertUserId(transaction.getInsertUserId());
      transactionNew.setUpdateUserId(transaction.getUpdateUserId());
      transactionList.add(1,transactionNew);

      /** to Taskに一時的にデータを作る*/
      List<Task> taskList = new ArrayList<Task>();
      for (Transaction task : transactionList) {
        Task createTask = Task.builder()
          .accountNumber(task.getAccountNumber())
          .payAccountNumber(task.getPayAccountNumber())
          .type(task.getType())
          .amount(task.getAmount())
          .poolFlag(task.getPoolFlag())
          .feeId(task.getFeeId())
          .balance(task.getBalance())
          .tradingDate(task.getTradingDate())
          .insertUserId(task.getInsertUserId())
          .updateUserId(task.getUpdateUserId())
          .build();
        taskList.add(createTask);
      }
      taskList.forEach(createTask -> taskService.create(createTask));
    }

    if (transaction.getType() == 1 || transaction.getType() == 2) {
      /** to Taskに一時的にデータを作る*/
      Task createTask = Task.builder()
        .id(transaction.getId())
        .accountNumber(transaction.getAccountNumber())
        .payAccountNumber(transaction.getPayAccountNumber())
        .type(transaction.getType())
        .amount(transaction.getAmount())
        .poolFlag(transaction.getPoolFlag())
        .feeId(transaction.getFeeId())
        .balance(transaction.getBalance())
        .tradingDate(transaction.getTradingDate())
        .insertUserId(transaction.getInsertUserId())
        .updateUserId(transaction.getUpdateUserId())
        .build();
      taskService.create(createTask);
    }
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