package com.example.demo.repository;

import com.example.demo.entity.MstUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface MstUserRepository extends
        JpaRepository<MstUser, Long>, JpaSpecificationExecutor<MstUser> {
}
