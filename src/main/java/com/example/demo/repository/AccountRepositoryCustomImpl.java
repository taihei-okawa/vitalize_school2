package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Account;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.demo.repository.AccountRepositoryCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

	@Autowired
	EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> search(String accountNumber, String clientId, String branchCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.account_number,a.cliant_id,a.branch_code From Account a WHERE ");
		boolean andFlg = false;
		boolean accountNumberFlg = false;
		boolean clientIdFlg = false;
		boolean branchCodeFlg = false;

		if (!"".equals(accountNumber) && accountNumber != null) {
			sql.append(" a.account_number LIKE :accountNumber ");
			accountNumberFlg = true;
			andFlg = true;
		}

		if (!"".equals(clientId) && clientId != null) {
			if (andFlg) sql.append(" AND ");
			sql.append(" a.cliant_id LIKE :clientId ");
			clientIdFlg = true;
			andFlg = true;
		}

		if (!"".equals(branchCode) && branchCode != null) {
			if (andFlg) sql.append(" AND ");
			sql.append("a.branch_code LIKE :branchCode ");
			branchCodeFlg = true;
			andFlg = true;
		}


//		if (priceFrom != null) {
//			if (andFlg) sql.append(" AND ");
//			sql.append("g.price >= :priceFrom ");
//			priceFromFlg = true;
//			andFlg = true;
//		}
//		if (priceTo != null) {
//			if (andFlg) sql.append(" AND ");
//			sql.append("g.price <= :priceTo ");
//			priceToFlg = true;
//			andFlg = true;
//		}
		Query query = manager.createNativeQuery(sql.toString());
		if (accountNumberFlg) query.setParameter("accountNumber", "%" + accountNumber + "%");
		if (clientIdFlg) query.setParameter("clientId", "%" + clientId + "%");
		if (branchCodeFlg) query.setParameter("branchCode", "%" + branchCode + "%");
		return query.getResultList();
	}

}