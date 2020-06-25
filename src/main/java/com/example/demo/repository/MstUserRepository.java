package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.MstUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface MstUserRepository extends JpaRepository<MstUser, Long>, JpaSpecificationExecutor<MstUser> {
    public Page<MstUser> findAll(Pageable pageable);
}
