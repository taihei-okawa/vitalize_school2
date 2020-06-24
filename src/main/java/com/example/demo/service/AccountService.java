package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AccountRepositoryCustom;

@Service
public class AccountService {

	@Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountRepositoryCustom accountRepositoryCustom;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

	public List<Account> search(String accountNumber, String clientId, String branchCode) {
		List<Account> result;
		if ("".equals(accountNumber) && "".equals(clientId) && "".equals(branchCode)) {
			result = accountRepository.findAll();
		} else {
			result = accountRepositoryCustom.search(accountNumber, clientId, branchCode);
		}
		return result;
	}

    public Account findOne(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void delete(Long id) {
    	accountRepository.deleteById(id);
    }

}