package com.example.demo.service;

import com.example.demo.entity.MstUser;
import com.example.demo.repository.MstUserRepository;
import com.example.demo.searchform.MstUserSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = Exception.class)
public class MstUserService {

  @Autowired
  private MstUserRepository mstUserRepository;

  // 社員の内容とページネーションを全検索
  public Page<MstUser> getAll(Pageable pageable, MstUserSearchForm searchForm) {
    try {
      Integer.parseInt(searchForm.getId());
      Specification<MstUser> spec = Specification
              .where(userIdEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
              .and(nameContains(searchForm.getUserName() == null ? searchForm.getUserName() : searchForm.getUserName().replaceAll("　", "").replaceAll(" ", "")));
      return mstUserRepository.findAll(spec, pageable);
    } catch(NumberFormatException e) {
      searchForm.setId("");
      Specification<MstUser> spec = Specification
              .where(userIdEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
              .and(nameContains(searchForm.getUserName() == null ? searchForm.getUserName() : searchForm.getUserName().replaceAll("　", "").replaceAll(" ", "")));
      return mstUserRepository.findAll(spec, pageable);
    }
  }

  public List<MstUser> findAll() {
    return mstUserRepository.findAll();
  }

  public MstUser findOne(Long id) {
    return mstUserRepository.findById(id).orElse(null);
  }

  public MstUser save(MstUser mstUser) {
    return mstUserRepository.save(mstUser);
  }

  public void delete(Long id) {
    mstUserRepository.deleteById(id);
  }


  /**
   *  ID検索
   */
  private static Specification<MstUser> userIdEqual(String id) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return id == "" || Objects.isNull(id) ? null : (root, query, cb) -> {
      return cb.equal(root.get("id"),  id);
    };
  }

  /**
   *  ユーザー名検索
   */
  private static Specification<MstUser> nameContains(String userName) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return userName == "" || Objects.isNull(userName) ? null : (root, query, cb) -> {
      return cb.like(root.get("userName"), "%" + userName + "%");
    };
  }
}