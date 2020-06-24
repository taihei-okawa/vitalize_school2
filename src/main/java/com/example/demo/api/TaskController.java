package com.example.demo.api;

import java.util.List;
import java.util.Date;
import lombok.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /** to 取引履歴 データ移行*/
    @Scheduled(cron = "${scheduler.cron}", zone = "Asia/Tokyo")
    public void taskCronTimeZone() {
        List<Task> taskList = taskService.searchAll();
        List<Transaction> transactionList = new ArrayList<Transaction>();
        for (Task task : taskList) {
            Transaction transaction = Transaction.builder()
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
    /** to 取引履歴 全件取得*/
    @RequestMapping(method = RequestMethod.GET)
    public List<Task> getTask() {
        return taskService.searchAll();
    }

    /** to 取引履歴取得 口座番号検索*/
    @GetMapping(value = "seach/{accountNumber}")
    public Task getById(@PathVariable("accountNumber") Integer accountNumber) {
        return taskService.findOne(accountNumber);
    }

    /** to 営業時間外 入金出金処理 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Task postTask(@RequestBody Task task) {
        return taskService.create(task);
    }
}