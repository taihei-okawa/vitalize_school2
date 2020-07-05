package vitalize.school.bank.repository;

import vitalize.school.bank.entity.Account;
import vitalize.school.bank.entity.MstFee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface MstFeeRepository extends JpaRepository<MstFee, Long>, JpaSpecificationExecutor<MstFee> {
    public Page<MstFee> findAll(Pageable pageable);
    public List<MstFee> findByBranchCode(String branchCode);
}
