package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
/**
 * 取引履歴情報 Entity
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="transaction")
public class Transaction implements Serializable{
    /**
     * ID
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * 口座番号
     */
    @Column(name="account_number")
    private Integer accountNumber;
    /**
     * 振込先口座番号
     */
    @Column(name="pay_account_number")
    private Integer payAccountNumber;
    /**
     * 種別
     */
    @Column(name="type")
    private Integer type;
    /**
     * 保留フラグ
     */
    @Column(name="pool_flag")
    private Integer poolFlag;
    /**
     * 取引金額
     */
    @Column(name="amount")
    private Integer amount;
    /**
     * 手数料
     */
    @Column(name="fee_id")
    private Integer feeId;
    /**
     * 取引後残高
     */
    @Column(name="balance")
    private Integer balance;

    /**
     * 取引日
     */
    @Column(name="trading_date")
    private Date tradingDate;
    private String stringTradingDate;

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
     * 更新日時
     */
    @Column(name="update_date")
    private Date updateDate;
    /**
     * 登録日時
     */
    @Column(name="insert_date")
    private Date insertDate;

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