package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.demo.searchform.TransactionSearchForm;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.example.demo.repository.TransactionRepository;
import com.example.demo.entity.Transaction;
import org.thymeleaf.util.StringUtils;

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
    public List<Transaction> searchAll(){
        return new ArrayList<>();
    }

    public Page<Transaction> getAll(Pageable pageable, TransactionSearchForm searchForm) {
        Specification<Transaction> spec = Specification.where(idEqual(searchForm.getId()))
                .and(accountNumberLike(searchForm.getAccountNumber()));
        return transactionRepository.findAll(spec, pageable);
    }
    // 取引履歴の登録
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private Specification<Transaction> idEqual(String id){
        return id == "" || Objects.isNull(id) ? null : new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("id"), id);
            }
        };
    }

    private Specification<Transaction> accountNumberLike(String accountNumber){
        return accountNumber == "" || Objects.isNull(accountNumber) ? null : new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("accountNumber"), accountNumber);
            }
        };
    }
}