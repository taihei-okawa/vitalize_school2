package vitalize.school.bank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.Transaction;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    public Page<Account> findAll(Pageable pageable);
    public List<Account> findByClientId(Integer clientId);
    public List<Account>  findByAccountNumber(Integer accountNumber);
}