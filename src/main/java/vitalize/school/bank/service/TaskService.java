package vitalize.school.bank.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vitalize.school.bank.repository.TaskRepository;
import vitalize.school.bank.entity.Task;

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

  /**
   * to 取引履歴の内容を全検索
   */
  public List<Task> searchAll() {
    return taskRepository.findAll();
  }

  /**
   * to 取引履歴 登録
   */
  public Task create(Task task) {
    return taskRepository.save(task);
  }
  /**
   * to 振込 処理 自分の口座　最新レコード取得
   */
  public List<Task> findNumber(Integer accountNumber) {
    return taskRepository.findByAccountNumber(accountNumber);
  }

  /**
   * to 振込 処理 相手の口座　最新レコード取得
   */
  public List<Task> findPayNumber(Integer payAccountNumber) {
    return taskRepository.findByPayAccountNumber(payAccountNumber);
  }

  /**
   * to 取引履歴の口座で検索
   */
  public Task findOne(Integer accountNumber) {
    return taskRepository.findByAccountNumber(accountNumber).get(0);
  }
}