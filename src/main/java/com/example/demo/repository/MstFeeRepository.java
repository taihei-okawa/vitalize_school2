package com.example.demo.repository;

import com.example.demo.entity.MstFee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface MstFeeRepository extends JpaRepository<MstFee, Long>, JpaSpecificationExecutor<MstFee> {
    public Page<MstFee> findAll(Pageable pageable);
}
