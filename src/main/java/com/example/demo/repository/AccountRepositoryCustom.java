package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Account;
import java.util.List;

@Repository
public interface AccountRepositoryCustom {
	public List<Account> search(String accountNumber, String clientId, String branchCode);
}