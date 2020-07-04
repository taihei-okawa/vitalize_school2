package vitalize.school.bank.service;

import vitalize.school.bank.entity.Account;
import vitalize.school.bank.repository.AccountRepository;
import vitalize.school.bank.searchform.AccountSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  public List<Account> findAll() {
    return accountRepository.findAll();
  }
  public Account findOne(Long id) {
    return accountRepository.findById(id).orElse(null);
  }
  public List<Account> findClientId(Integer accountClientId) {
    return accountRepository.findByClientId(accountClientId);
  }

  public Account save(Account account) {
    return accountRepository.save(account);
  }

  public void delete(Long id) {
    accountRepository.deleteById(id);
  }

  // 口座機能の内容とページネーションを全検索
  public Page<Account> getAll(Pageable pageable, AccountSearchForm searchForm) {
    Specification<Account> spec = Specification
            .where(idEqual(searchForm.getId()))
            .and(numberEqual(searchForm.getAccountNumber()))
            .and(branchCodeContains(searchForm.getBranchCode()));
    return accountRepository.findAll(spec, pageable);
  }

  /**
   *  口座ID検索
   */
  private static Specification<Account> idEqual(String id) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return id == "" || Objects.isNull(id) ? null : (root, query, cb) -> {
      return cb.equal(root.get("id"),  id);
    };
  }

  /**
   *  口座番号検索
   */
  private static Specification<Account> numberEqual(String accountNumber) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return accountNumber == "" || Objects.isNull(accountNumber) ? null : (root, query, cb) -> {
      return cb.equal(root.get("accountNumber"), accountNumber);
    };
  }

  /**
   *  視点コード検索
   */
  private static Specification<Account> branchCodeContains(String branchCode) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return branchCode == "" || Objects.isNull(branchCode) ? null : (root, query, cb) -> {
      return cb.like(root.get("branchCode"), "%" + branchCode + "%");
    };
  }
}

