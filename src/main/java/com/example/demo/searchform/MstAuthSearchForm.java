package com.example.demo.searchform;

import lombok.*;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Getter
@Setter
public class MstAuthSearchForm  implements java.io.Serializable{
    private String id;
    private String status;
}
