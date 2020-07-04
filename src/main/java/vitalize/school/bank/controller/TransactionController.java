package vitalize.school.bank.controller;

import java.text.ParsePosition;
import java.util.*;
import java.text.SimpleDateFormat;
import vitalize.school.bank.searchform.TransactionSearchForm;

import vitalize.school.bank.entity.Task;
import vitalize.school.bank.entity.Transaction;
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
  @Autowired
  private TaskService taskService;

  /**
   * 取引履歴機能情報 Service
   */
  @Autowired
  private TransactionService transactionService;

  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  @GetMapping(value = "/list")
  /** to 取引履歴機能 一覧画面表示*/
  /** to 取引履歴機能 ページネーション*/
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
   * to 取引履歴機能 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    return "transaction/add";
  }

  /**
   * to 取引履歴 process 登録
   */
  @Transactional
  @PostMapping(value = "/add")
  public String create( @ModelAttribute Transaction transaction) {
    List<Task> TaskList = taskService.findNumber(transaction.getPayAccountNumber());
    transaction.setInsertUserId(9001);
    transaction.setUpdateUserId(9001);

    /**
     * to 処理　判断 取引履歴 process 登録
     */
    //入金
    if (transaction.getType() == 1){
      transaction.setAccountNumber(transaction.getPayAccountNumber());
      Integer amount = transaction.getAmount();
      Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      Integer balance = MaxTaskList.getBalance();
      Integer answer;
      answer = balance + amount;
      transaction.setBalance(answer);
    }
    //出金
    if (transaction.getType() == 2) {
      transaction.setAccountNumber(transaction.getPayAccountNumber());
      Integer amount = transaction.getAmount();
      Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      Integer balance = MaxTaskList.getBalance();
      Integer answer;
      answer = balance - amount;
      transaction.setBalance(answer);
    }
    //振込
    if (transaction.getType() == 3) {

      /** to 日付型に変換*/
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
      Date date = sdf.parse(transaction.getStringTradingDate(), new ParsePosition(0));
      transaction.setTradingDate(date);

      /** to 自分の口座　出金処理 */
      Integer amount = transaction.getAmount();
      Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
      Integer balance = MaxTaskList.getBalance();
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
      /** to 日付型に変換*/
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
      Date date = sdf.parse(transaction.getStringTradingDate(), new ParsePosition(0));
      transaction.setTradingDate(date);

      /** to 手数料計算 */


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