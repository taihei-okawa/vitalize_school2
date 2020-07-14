package vitalize.school.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vitalize.school.bank.entity.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 取引履歴情報 Repository
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    public Page<Transaction> findAll(Pageable pageable);
    public Integer deleteByAccountNumber(Integer accountNumber);
}
