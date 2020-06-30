package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.MstAuth;

import com.example.demo.repository.MstAuthRepository;
import com.example.demo.searchform.MstAuthSearchForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 権限一覧情報 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class MstAuthService {

  /**
   * 権限情報 Repository
   */
  @Autowired
  private MstAuthRepository mstAuthRepository;

  /**
   * 取引履歴機能の内容とページネーションを全検索
   */
  public Page<MstAuth> getAll(Pageable pageable, MstAuthSearchForm searchForm) {
    Specification<MstAuth> spec = Specification.where(authIdEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
            .and(statusNameEqual(searchForm.getStatusName() == null ? searchForm.getStatusName() : searchForm.getStatusName().replaceAll("　", "").replaceAll(" ", "")));
    return mstAuthRepository.findAll(spec, pageable);
  }

  // 権限の内容を全検索
  public List<MstAuth> searchAll() {
    return mstAuthRepository.findAll();
  }

  public MstAuth findOne(Long id) {
    return mstAuthRepository.findById(id).orElse(null);
  }

  /**
   *  ID検索
   */
  private static Specification<MstAuth> authIdEqual(String id) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return id == "" || Objects.isNull(id) ? null : (root, query, cb) -> {
      return cb.equal(root.get("id"),  id);
    };
  }

  /**
   *  権限名検索
   */
  private static Specification<MstAuth> statusNameEqual(String statusName) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return statusName == "" || Objects.isNull(statusName) ? null : (root, query, cb) -> {
      return cb.like(root.get("statusName"), "%" + statusName + "%");
    }
  }}