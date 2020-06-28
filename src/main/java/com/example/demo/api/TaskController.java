package com.example.demo.api;

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

import com.example.demo.entity.Task;
import com.example.demo.entity.Transaction;
import com.example.demo.service.TaskService;
import com.example.demo.service.TransactionService;

@Component
@RestController
@Transactional
@RequestMapping("/task")
public class TaskController {
  @Autowired
  private TaskService taskService;
  @Autowired
  private TransactionService transactionService;

  /** to 取引履歴 指定実行*/

  /**
   * to 営業時間開始　取引履歴 データ移行
   */
  @Scheduled(cron = "${scheduler.cron}", zone = "Asia/Tokyo")
  public void taskCronTimeZone() {
    List<Task> taskList = taskService.searchAll();
    taskList.stream()
      .filter(tk -> tk.getTradingDate() == null && tk.getPoolFlag() == 1)
      .collect(Collectors.toList());
    List<Transaction> transactionList = new ArrayList<Transaction>();
    for (Task task : taskList) {
      Transaction transaction = Transaction.builder()
        .id(task.getId())
        .accountNumber(task.getAccountNumber())
        .payAccountNumber(task.getPayAccountNumber())
        .type(task.getType())
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
  @GetMapping(value = "seach/{accountNumber}")
  public Task getById(@PathVariable("accountNumber") Integer accountNumber) {
    return taskService.findOne(accountNumber);
  }

//  /**
//   * to 振込 処理(旧)
//   */
//  @PostMapping
//  @ResponseStatus(HttpStatus.CREATED)
//  Task postTask(@RequestBody Task task) {
//    Integer accountNumber = task.getAccountNumber();
//    return taskService.create(task);
//  }

  /**
   * to 振込 処理
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Task save(@RequestBody Task task) {
    /** to 自分の口座　出金処理 */
    Integer amount = task.getAmount();
    List<Task> TaskList = taskService.findNumber(task.getAccountNumber());
    Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
    Integer balance = MaxTaskList.getBalance();
    Integer answer;
    answer = balance - amount;
    //値を追加する
    task.setType(2);
    task.setBalance(answer);
    return taskService.create(task);
//    /** to 相手の口座　入金処理 */
//    List<Task> TaskPayList = taskService.findPayNumber(task.getPayAccountNumber());
//    Task MaxTaskPayList = TaskPayList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
//    Integer payBalance = MaxTaskPayList.getBalance();
//    Integer payAnswer;
//    payAnswer = payBalance + amount;
//    task.setType(1);
//    task.setBalance(payAnswer);
//    return taskService.create(task);
  }
}