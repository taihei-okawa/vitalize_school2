package com.example.demo.service;

import com.example.demo.entity.MstAuth;
import com.example.demo.repository.MstAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

  // 権限の内容を全検索
  public List<MstAuth> searchAll() {
    return mstAuthRepository.findAll();
  }

  public MstAuth findOne(Long id) {
    return mstAuthRepository.findById(id).orElse(null);
  }

  /**
   * 取引履歴機能の内容とページネーションを全検索
   */
  public Page<MstAuth> getAll(Pageable pageable) {
    return mstAuthRepository.findAll(pageable);
  }
}