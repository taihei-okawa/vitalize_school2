package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Task;
import lombok.RequiredArgsConstructor;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.example.demo.repository.TransactionRepository;
import com.example.demo.entity.Transaction;

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
    // 取引履歴機能の内容を全検索
    public List<Transaction> searchAll() {
        return transactionRepository.findAll();
    }
    // 取引履歴機能の内容とページネーションを全検索
    public Page<Transaction> getAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }
    // 取引履歴の登録
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}