package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.Task;

import java.util.List;

/**
 * 取引履歴情報 Repository
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
//    public List<Task> findAccountNumber(Integer accountNumber);
//    public List<Task> findByPayAccountNumber(Integer payAccountNumber);
    public Task findByAccountNumber(Integer accountNumber);
}
