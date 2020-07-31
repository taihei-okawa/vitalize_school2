package vitalize.school.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 権限マスタ情報 Entity
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name="mst_auth")
public class MstAuth extends BaseEntity implements Serializable {
    /**
     * 権限ID
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "auth_code", nullable = false, unique = true)
    private String authCode;

    @ManyToMany
    @JoinTable(
      name = "user_auth",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "auth_id"))
    private List<MstUser> MstUsers;

    /**
     * ステータス
     */
    @Column(name="status")
    private String status;

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
    /**
     * 削除日時
     */
    @Column(name="delete_date")
    private Date deleteDate;
}
