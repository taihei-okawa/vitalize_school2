package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.ManyToOne;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 *口座機能 Entity
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id")
    private Long id;

	@Size(max = 7)
	@NotNull
    @Column(name = "account_number")
    @Getter
    @Setter
    private Integer accountNumber;

    @Column(name = "client_id")
    @Getter
    @Setter
    private Integer clientId;

	@NotEmpty
    @Column(name = "branch_code")
    @Getter
    @Setter
    private String branchCode;

    @Column(name="insert_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private Date insertDate;

    @Column(name = "insert_user_id")
    @Getter
    @Setter
    private Integer insertUserId;

    @Column(name = "update_user_id")
    @Getter
    @Setter
    private Integer updateUserId;

    @PrePersist
    public void onPrePersist() {
        setInsertDate(new Date());
    }
}
