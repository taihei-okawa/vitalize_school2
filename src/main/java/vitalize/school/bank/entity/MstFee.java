package vitalize.school.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * 情報 Entity
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_fee")
public class MstFee implements Serializable {
  /**
   * 手数料ID
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 手数料額
   */
  @NotNull
  @Min(0)
  @Max(1000)
  @Column(name = "fee_price")
  private Integer feePrice;

  /**
   * 支店名
   */
  @Column(name = "branch_code")
  private String branchCode;
  /**
   * 営業日フラグ
   */
  @Column(name = "business_frag")
  private String businessFrag;
  /**
   * 取引開始時間
   */
  @NotEmpty
  @Column(name = "start_day")
  private String startDay;
  /**
   * 取引時間終了
   */
  @NotEmpty
  @Column(name = "end_day")
  private String endDay;
  /**
   * 登録者
   */
  @Column(name = "insert_user_id")
  private Integer insertUserId;

  /**
   * 更新者
   */
  @Column(name = "update_user_id")
  private Integer updateUserId;

  /**
   * 登録日時
   */
  @Column(name = "insert_date", updatable=false)
  private Date insertDate;

  /**
   * 更新日時
   */
  @Column(name = "update_date")
  private Date updateDate;

  @PrePersist
  public void onPrePersist() {
    setInsertDate(new Date());
    setUpdateDate(new Date());
  }

  @PreUpdate
  public void onPreUpdate() {
    setUpdateDate(new Date());
  }

  public void addObject(String column, String column1) {
  }
  public void setUpdateId(int i) {
  }
}