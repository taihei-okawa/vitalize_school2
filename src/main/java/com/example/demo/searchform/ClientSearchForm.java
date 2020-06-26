package com.example.demo.searchform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientSearchForm implements java.io.Serializable{
    private String id;
    private String clientName;
    private String clientNameKana;
}
