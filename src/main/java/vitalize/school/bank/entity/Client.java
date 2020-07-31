package vitalize.school.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 情報 Entitygit a
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class Client extends BaseEntity implements Serializable {
  /**
   * 顧客ID
   */
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 顧客名
   */
  @NotEmpty
  @Size(min=1, max=20)
  @Column(name = "client_name")
  private String clientName;
  /**
   * 顧客名フリガナ
   */
  @Column(name = "client_name_kana")
  private String clientNameKana;
  /**
   * 電話番号
   */
  @Size(min=11, max=12)
  @Column(name = "tell")
  private String tell;
  /**
   * メールアドレス
   */
  @NotEmpty
  @Email
  @Column(name = "mail_address")
  private String mailAddress;
  /**
   * パスワード
   */
  @NotEmpty
  @Column(name = "password")
  private String password;
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
  @Column(name = "insert_date", updatable = false)
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