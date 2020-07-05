package vitalize.school.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
   * 手数料コード
   */
  @Column(name = "fee_code")
  private String feeCode;

  /**
   * 手数料額
   */
  @Column(name = "fee_price")
  private Integer feePrice;

  /**
   * 銀行コード
   */
  @Column(name = "bank_code")
  private String bankCode;
  /**
   * 営業日
   */
  @Column(name = "business_day")
  private String businessDay;
  /**
   * 休日
   */
  @Column(name = "holiday")
  private String holiday;
  /**
   * 取引開始時間
   */
  @Column(name = "start_day")
  private String startDay;
  /**
   * 取引時間終了
   */
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