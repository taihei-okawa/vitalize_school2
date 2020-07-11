package vitalize.school.bank.service;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.MstFee;
import vitalize.school.bank.entity.Task;
import vitalize.school.bank.repository.TransactionRepository;
import vitalize.school.bank.entity.Transaction;
import vitalize.school.bank.searchform.TransactionSearchForm;

/**
 * 取引履歴機能 一覧画面表示 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class TransactionService {

  /**
   * 取引履歴機能 Repository
   */
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private TaskService taskService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private MstFeeService mstFeeService;

  /**
   * 取引履歴  ダウンロード検索 Repository
   */
  public List<Transaction> searchAll(TransactionSearchForm searchForm) {
    Specification<Transaction> spec = Specification.where(idEqual(searchForm.getId()))
            .and(accountNumberLike(searchForm.getAccountNumber()));
    return transactionRepository.findAll(spec);
  }
  /**
   * 取引履歴 一覧検索 Repository
   */
  public Page<Transaction> getAll(Pageable pageable, TransactionSearchForm searchForm) {
    try {
      // 文字列から数字変換できるか判定
      Integer.parseInt(searchForm.getId());
      Integer.parseInt(searchForm.getAccountNumber());

      Specification<Transaction> spec = Specification
              .where(idEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
              .and(accountNumberLike(searchForm.getAccountNumber() == null ? searchForm.getAccountNumber() : searchForm.getAccountNumber().replaceAll("　", "").replaceAll(" ", "")));
      return transactionRepository.findAll(spec, pageable);
    } catch(NumberFormatException e) {
      Specification<Transaction> spec = Specification.where(null);
      return transactionRepository.findAll(spec, pageable);
    }
  }

  /**
   * 取引履歴 登録　Repository
   */
  public Transaction save(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  private Specification<Transaction> idEqual(String id) {
    return id == "" || Objects.isNull(id) ? null : new Specification<Transaction>() {
      @Override
      public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("id"), id);
      }
    };
  }

  /**
   * 取引履歴 LIKE検索　Repository
   */
  private Specification<Transaction> accountNumberLike(String accountNumber) {
    return accountNumber == "" || Objects.isNull(accountNumber) ? null : new Specification<Transaction>() {
      @Override
      public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("accountNumber"), accountNumber);
      }
    };
  }
  public void AccountPay(Transaction transaction) throws ParseException {
    /** to 本日日付に代入*/
    if (transaction.getStringTradingDate().isEmpty()) {
      Date date = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      String strDate = dateFormat.format(date);
      transaction.setStringTradingDate(strDate);
    }else{
      String time = ":00";
      String Trading= transaction.getStringTradingDate();
      String strTime = Trading.concat(time);
      transaction.setStringTradingDate(strTime);
      transaction.setPoolFlag(1);
    }

    /** to 取引履歴の最新の情報だけを取得 */
    List<Task> TaskList = taskService.findNumber(transaction.getAccountNumber());
    Integer amount = transaction.getAmount();
    Task MaxTaskList = TaskList.stream().max(Comparator.comparing(tk -> tk.getId())).get();
    Integer balance = MaxTaskList.getBalance();

    /** to 手数料計算 */
    //口座テーブルに口座番号で支店名を検索
    Integer accountNumber = transaction.getAccountNumber();
    List<Account> Account = accountService.findAccount(accountNumber);
    //手数料テーブルに支店名で検索
    String branchCode = Account.get(0).getBranchCode();
    List<MstFee> mstFeeList = mstFeeService.findBranchCode(branchCode);
    //曜日判断
    String strTrading = transaction.getStringTradingDate().substring(0, 10);
    mstFeeList.stream()
      .filter(msl -> msl.getStartDay().contains(strTrading) || msl.getStartDay().contains(strTrading)|| transaction.getPoolFlag()==1)
      .collect(Collectors.toList());
    Integer feePrice = 0;
    if(mstFeeList == null && mstFeeList.size() == 0){
      feePrice = 0;
    }else{
      //時間判断
      for(MstFee fee:mstFeeList) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date start = format.parse(fee.getStartDay());
        Date end = format.parse(fee.getEndDay());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date todayDate = sdf.parse(transaction.getStringTradingDate());
        if(start.before(todayDate) && end.after(todayDate)){
          feePrice = mstFeeList.get(0).getFeePrice();
        }
      }
    }
    /** to 日付型に変換*/
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date = sdf.parse(transaction.getStringTradingDate());
    transaction.setTradingDate(date);
    transaction.setInsertUserId(9001);
    transaction.setUpdateUserId(9001);

    /**
     * to 処理　判断 取引履歴 process 登録
     */
    //入金
    if (transaction.getType() == 1) {
      transaction.setPayAccountNumber(transaction.getAccountNumber());
      Integer answer;
      answer = balance + amount - feePrice;
      transaction.setBalance(answer);
      transaction.setFeeId(feePrice);
    }
    //出金
    if (transaction.getType() == 2) {
      transaction.setPayAccountNumber(transaction.getAccountNumber());
      Integer answer;
      answer = balance - amount - feePrice;
      if(Math.signum(answer) == -1.0) {
        System.out.println("残高足りないので実行できません");
      }else{
        transaction.setBalance(answer);
        transaction.setFeeId(feePrice);
      }
    }
    //振込
    if (transaction.getType() == 3) {
      /** to 自分の口座　出金処理 */
      Integer answer;
      answer = balance - amount - feePrice;
      transaction.setBalance(answer);
      transaction.setFeeId(feePrice);
      List<Transaction> transactionList = new ArrayList<Transaction>();
      transactionList.add(0, transaction);

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
      transactionNew.setBalance(payAnswer);
      transactionNew.setStringTradingDate(transaction.getStringTradingDate());
      transactionNew.setTradingDate(transaction.getTradingDate());
      transactionNew.setInsertUserId(transaction.getInsertUserId());
      transactionNew.setUpdateUserId(transaction.getUpdateUserId());
      transactionList.add(1, transactionNew);

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
          .stringTradingDate(task.getStringTradingDate())
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
        .stringTradingDate(transaction.getStringTradingDate())
        .tradingDate(transaction.getTradingDate())
        .insertUserId(transaction.getInsertUserId())
        .updateUserId(transaction.getUpdateUserId())
        .build();
      taskService.create(createTask);
    }
  }
}