package vitalize.school.bank.api;

import java.util.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import vitalize.school.bank.entity.Task;
import vitalize.school.bank.entity.Transaction;
import vitalize.school.bank.service.TaskService;
import vitalize.school.bank.service.TransactionService;

@Transactional
@Component
@RestController
@RequestMapping("/task")
public class TaskController {
  @Autowired
  private TaskService taskService;
  @Autowired
  private TransactionService transactionService;

  /** to 取引履歴 指定実行*/

  /**
   * to 営業時間開始　取引履歴 営業時間外データ移行
   */
  @Scheduled(cron = "${scheduler.cron}", zone = "Asia/Tokyo")
  public void taskCronTimeZone() {
    List<Task> taskList = taskService.searchAll();
    taskList.stream()
      .filter(tk -> tk.getPoolFlag() == 1)
      .collect(Collectors.toList());
    List<Transaction> transactionList = new ArrayList<Transaction>();
    for (Task task : taskList) {
      Transaction transaction = Transaction.builder()
        .id(task.getId())
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
        .insertDate(task.getInsertDate())
        .updateDate(task.getUpdateDate())
        .build();
      transactionList.add(transaction);
    }
    transactionList.forEach(transaction -> transactionService.save(transaction));
  }

  /**
   * to 営業時間内　取引履歴 データ移行
   */
  @Scheduled(cron = "${scheduler.today}", zone = "Asia/Tokyo")
  public void taskTimeZone() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    String today = sdf.format(timestamp);
    List<Task> taskList = taskService.searchAll();
    taskList.stream()
      .filter(tk -> tk.getTradingDate() != null && tk.getTradingDate().toString() == today)
      .collect(Collectors.toList());
    List<Transaction> transactionList = new ArrayList<Transaction>();
    for (Task task : taskList) {
      Transaction transaction = Transaction.builder()
        .id(task.getId())
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
        .insertDate(task.getInsertDate())
        .updateDate(task.getUpdateDate())
        .build();
      transactionList.add(transaction);
    }
    transactionList.forEach(transaction -> transactionService.save(transaction));
  }

  /** todo ユーザー認証必要 */

  /**
   * to 取引履歴 全件取得
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<Task> getTask() {
    return taskService.searchAll();
  }

  /**
   * to 取引履歴　残高確認
   */
  @GetMapping(value = "search/{accountNumber}")
  public Task getById(@PathVariable("accountNumber") Integer accountNumber) {
    return taskService.findOne(accountNumber);
  }

  /**
   * to 振込 処理
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  void save(@RequestBody Task task) {
    /** to 自分の口座　出金処理 */
    Integer amount = task.getAmount();
    List<Task> TaskList = taskService.findNumber(task.getAccountNumber());
    Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
    Integer balance = MaxTaskList.getBalance();
    Integer answer;
    answer = balance - amount;
    task.setType(3);
    task.setBalance(answer);
    List<Task> taskList = new ArrayList<Task>();
    taskList.add(0, task);

    /** to 相手の口座　入金処理 */
    List<Task> TaskPayList = taskService.findPayNumber(task.getPayAccountNumber());
    Task MaxTaskPayList = TaskPayList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
    Integer payBalance = MaxTaskPayList.getBalance();
    Integer payAnswer;
    payAnswer = payBalance + amount;
    Task taskNew = new Task();
    List<Task> taskNewList = new ArrayList<Task>();
    taskNew.setAccountNumber(task.getPayAccountNumber());
    taskNew.setPayAccountNumber(task.getAccountNumber());
    taskNew.setPoolFlag(task.getPoolFlag());
    taskNew.setAmount(task.getAmount());
    taskNew.setBalance(payAnswer);
    taskNew.setType(3);
    taskNew.setInsertUserId(task.getInsertUserId());
    taskNew.setUpdateUserId(task.getUpdateUserId());
    taskList.add(1, taskNew);
    taskList.stream().forEach(taskSave -> taskService.create(taskSave));
  }
}