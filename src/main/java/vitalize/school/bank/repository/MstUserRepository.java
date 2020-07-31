package vitalize.school.bank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vitalize.school.bank.entity.MstUser;

import java.util.Optional;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface MstUserRepository extends JpaRepository<MstUser, Long>, JpaSpecificationExecutor<MstUser> {
    public Page<MstUser> findAll(Pageable pageable);
    Optional<MstUser> findByUserNameAndDeleteDateIsNull(String userName);
}
