package com.example.demo.controller;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.demo.searchform.TransactionSearchForm;

import com.example.demo.entity.Task;
import com.example.demo.entity.Transaction;
import com.example.demo.service.TransactionService;
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
import com.example.demo.service.TaskService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.web.bind.annotation.ResponseBody;


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
   * to 取引履歴機能 process 登録
   */
  @PostMapping(value = "/add")
  public String create(@ModelAttribute Transaction transaction) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    transaction.setInsertUserId(9001);
    transaction.setUpdateUserId(9001);
    //振込の仕方別
    if (transaction.getType() == 1 || transaction.getType() == 2) {
      transaction.setAccountNumber(transaction.getPayAccountNumber());
    }
    //最短日付か指定の日付か
    if (transaction.getStringTradingDate() == "") {
      Date date = new Date();
      sdf.format(date);
      transaction.setTradingDate(date);
    }
    try {
      Date date = sdf.parse(transaction.getStringTradingDate());
      transaction.setTradingDate(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Task createTask = Task.builder()
      .id(transaction.getId())
      .accountNumber(transaction.getAccountNumber())
      .payAccountNumber(transaction.getPayAccountNumber())
      .type(transaction.getType())
      .poolFlag(transaction.getPoolFlag())
      .feeId(transaction.getFeeId())
      .balance(transaction.getBalance())
      .tradingDate(transaction.getTradingDate())
      .insertUserId(transaction.getInsertUserId())
      .updateUserId(transaction.getUpdateUserId())
      .insertDate(transaction.getInsertDate())
      .updateDate(transaction.getUpdateDate())
      .build();
    taskService.create(createTask);
    return "redirect:/transaction/list";
  }
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