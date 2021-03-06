package vitalize.school.bank.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 情報 Entity
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_user")
public class MstUser extends BaseEntity implements Serializable {
  /**
   * 社員ID
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 社員名
   */
  @NotEmpty
  @Column(name = "user_name")
  private String userName;

  /**
   * パスワード
   */
  @NotEmpty
  @Column(name = "password")
  private String password;

  @ManyToMany(mappedBy = "MstUsers")
  private List<MstAuth> MstAuths;

  /**
   * ステータス
   */
  @Column(name = "status")
  private Integer status;

  /**
   * 支店名
   */
  @Column(name = "branch_code")
  private String branchCode;

  /**
   * 役職名
   */
  @Column(name = "position_code")
  private String positionCode;

  /**
   * 業務名
   */
  @Column(name = "business_code")
  private String businessCode;

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

  /**
   * 削除日時
   */
  @Column(name = "delete_date")
  private Date deleteDate;

  @PrePersist
  public void onPrePersist() {
    setInsertDate(new Date());
    setUpdateDate(new Date());
  }
  @PreUpdate
  public void onPreUpdate() {
    setUpdateDate(new Date());
  }

}