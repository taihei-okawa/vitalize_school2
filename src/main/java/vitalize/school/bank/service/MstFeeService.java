package vitalize.school.bank.service;

import vitalize.school.bank.entity.MstFee;
import vitalize.school.bank.repository.MstFeeRepository;
import vitalize.school.bank.searchform.MstFeeSearchForm;
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
public class MstFeeService {

  @Autowired
  private MstFeeRepository mstFeeRepository;

  // 社員の内容とページネーションを全検索
  public Page<MstFee> getAll(Pageable pageable, MstFeeSearchForm searchForm) {
    String feeCode = searchForm.getFeeCode() == null ? searchForm.getFeeCode() : searchForm.getFeeCode().replaceAll("　", "").replaceAll(" ", "");

    try {
      // idを文字列から数字変換できるか判定
      Integer.parseInt(searchForm.getId());

      Specification<MstFee> spec = Specification
              .where(mstFeeIdEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
              .and(feeCodeContains(feeCode));
      return mstFeeRepository.findAll(spec, pageable);
    } catch (NumberFormatException e) {
      Specification<MstFee> spec = Specification.where(feeCodeContains(feeCode));
      return mstFeeRepository.findAll(spec, pageable);
    }
  }

  public List<MstFee> findAll() {
    return mstFeeRepository.findAll();
  }

  public MstFee findOne(Long id) {
    return mstFeeRepository.findById(id).orElse(null);
  }

  public MstFee save(MstFee mstFee) {
    return mstFeeRepository.save(mstFee);
  }

  public void delete(Long id) {
    mstFeeRepository.deleteById(id);
  }


  /**
   *  ID検索
   */
  private static Specification<MstFee> mstFeeIdEqual(String id) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return id == "" || Objects.isNull(id) ? null : (root, query, cb) -> {
      return cb.equal(root.get("id"),  id);
    };
  }

  /**
   *  ユーザー名検索
   */
  private static Specification<MstFee> feeCodeContains(String feeCode) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return feeCode == "" || Objects.isNull(feeCode) ? null : (root, query, cb) -> {
      return cb.like(root.get("feeCode"), "%" + feeCode + "%");
    };
  }
}