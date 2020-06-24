package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.util.ArrayList;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import javax.persistence.OneToMany;

/**
 * 情報 Entity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="client")
public class Client implements Serializable {
  /**
   * 顧客ID
   */
  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  /**
   * 顧客名
   */
  @Column(name="client_name")
  private String clientName;
  /**
   * 顧客名フリガナ
   */
  @Column(name="client_name_kana")
  private String clientNameKana;
  /**
   * 電話番号
   */
  @Column(name="tell")
  private String tell;
  /**
   * メールアドレス
   */
  @Column(name="mail_address")
  private String mailAddress;
  /**
   * パスワード
   */
  @Column(name="password")
  private String password;
  /**
   * 登録者
   */
  @Column(name="insert_user_id")
  private Integer insertUserId;
  /**
   * 更新者
   */
  @Column(name="update_user_id")
  private Integer updateUserId;

  /**
   * 登録日時
   */
  @Column(name="insert_date", updatable=false)
  private Date insertDate;
  /**
   * 更新日時
   */
  @Column(name="update_date")
  private Date updateDate;
  /**
   * 削除日時
   */
  @Column(name="delete_date")
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