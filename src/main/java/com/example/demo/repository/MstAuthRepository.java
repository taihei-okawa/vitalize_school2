package com.example.demo.repository;

import com.example.demo.entity.MstUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.MstAuth;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface MstAuthRepository extends JpaRepository<MstAuth, Long>, JpaSpecificationExecutor<MstAuth> {
    public Page<MstAuth> findAll(Pageable pageable);
}
