package vitalize.school.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
  private Integer insertUserId;
  private Integer updateUserId;
  private Date insertDate;
  private Date updateDate;
  private Date deleteDate;
}
