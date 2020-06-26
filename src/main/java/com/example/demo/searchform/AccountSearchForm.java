package com.example.demo.searchform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSearchForm implements java.io.Serializable{
    private String id;
    private String accountNumber;
    private String branchCode;
}
