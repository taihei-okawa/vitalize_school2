package vitalize.school.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vitalize.school.bank.entity.Task;

import java.util.List;

/**
 * 取引履歴情報 Repository
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    public List<Task> findByAccountNumber(Integer accountNumber);
    public List<Task> findByPayAccountNumber(Integer payAccountNumber);
    public Integer deleteByAccountNumber(Integer accountNumber);
}
