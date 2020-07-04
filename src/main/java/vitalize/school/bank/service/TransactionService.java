package vitalize.school.bank.service;

import java.util.List;
import java.util.Objects;


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

  /**
   * 取引履歴  ダウンロード検索 Repository
   */
  public List<Transaction> searchAll(TransactionSearchForm searchForm) {
    Specification<Transaction> spec = Specification.where(idEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
            .and(accountNumberLike(searchForm.getAccountNumber() == null ? searchForm.getAccountNumber() : searchForm.getAccountNumber().replaceAll("　", "").replaceAll(" ", "")));
    return transactionRepository.findAll(spec);
  }
  /**
   * 取引履歴 一覧検索 Repository
   */
  public Page<Transaction> getAll(Pageable pageable, TransactionSearchForm searchForm) {
    Specification<Transaction> spec = Specification.where(idEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
      .and(accountNumberLike(searchForm.getAccountNumber() == null ? searchForm.getAccountNumber() : searchForm.getAccountNumber().replaceAll("　", "").replaceAll(" ", "")));
    return transactionRepository.findAll(spec, pageable);
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
}