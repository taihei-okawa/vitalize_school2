package vitalize.school.bank.service;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import vitalize.school.bank.entity.MstAuth;
import vitalize.school.bank.repository.MstAuthRepository;
import vitalize.school.bank.searchform.MstAuthSearchForm;
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
  public Page<MstAuth> searchAll(Pageable pageable, MstAuthSearchForm searchForm) {
    Specification<MstAuth> spec = Specification.where(authIdEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
            .and(statusNameEqual(searchForm.getStatusName() == null ? searchForm.getStatusName() : searchForm.getStatusName().replaceAll("　", "").replaceAll(" ", "")));
    return mstAuthRepository.findAll(spec, pageable);
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
    };
  }}