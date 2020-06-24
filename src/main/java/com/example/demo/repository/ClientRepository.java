package com.example.demo.repository;

import com.example.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 権限マスタ情報 Repository
 */

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {}
