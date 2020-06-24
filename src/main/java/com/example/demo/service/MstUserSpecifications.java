package com.example.demo.service;

import org.springframework.data.jpa.domain.Specification;
import com.example.demo.entity.MstUser;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.*;
import org.springframework.util.StringUtils;

@Service
public class MstUserSpecifications {

    /**
     *  ID検索
     */
    public static Specification<MstUser> userIdContains(Long id) {
        // ラムダ式で記述すると、引数のデータ型の指定が省略できる
        return StringUtils.isEmpty(id) ? null : (root, query, cb) -> {
            return cb.like(root.get("id"), "%" + id + "%");
        };
    }

    /**
     *  ユーザー名検索
     */
    public static Specification<MstUser> nameContains(String userName) {
        // ラムダ式で記述すると、引数のデータ型の指定が省略できる
        return StringUtils.isEmpty(userName) ? null : (root, query, cb) -> {
            return cb.like(root.get("userName"), "%" + userName + "%");
        };
    }

    /**
     *  ユーザー名検索
     */
    public static Specification<MstUser> branchCodeContains(Integer branchCode) {
        // ラムダ式で記述すると、引数のデータ型の指定が省略できる
        return StringUtils.isEmpty(branchCode) ? null : (root, query, cb) -> {
            return cb.like(root.get("branchCode"), "%" + branchCode + "%");
        };
    }
}
