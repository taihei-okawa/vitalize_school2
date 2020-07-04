package vitalize.school.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 権限マスタ情報 Entity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="mst_auth")
public class MstAuth implements Serializable {
    /**
     * 権限ID
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * 管理権限ステータス
     */
    @Column(name="status")
    private Integer status;
    /**
     * 管理権限名
     */
    @Column(name="status_name")
    private String statusName;
    /**
     * 機能ステータス
     */
    @Column(name="function_status")
    private Integer functionStatus;
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

    public Long getAuthId() {
        return id;
    }

    public void setAuthId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getFunctionStatus() {
        return functionStatus;
    }
    public void setFunctionStatus(Integer functionStatus) {
        this.functionStatus = functionStatus;
    }

}
