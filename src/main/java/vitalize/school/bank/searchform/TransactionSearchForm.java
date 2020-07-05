package vitalize.school.bank.searchform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionSearchForm implements java.io.Serializable{
    private String id;
    private String accountNumber;
}
