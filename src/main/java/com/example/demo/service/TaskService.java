package com.example.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.TaskRepository;
import com.example.demo.entity.Task;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.entity.Transaction;

/**
 * 取引履歴機能 一覧画面表示 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class TaskService {
/**
* 取引履歴機能 Repository
*/
@Autowired
private TaskRepository taskRepository;
    // 取引履歴の内容を全検索
    public List<Task> searchAll() {
        return taskRepository.findAll();
    }
    // 取引履歴 登録
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    // 取引履歴の内容を口座で検索
    public Task findOne(Integer accountNumber) {
        return taskRepository.findByAccountNumber(accountNumber);
    }

}