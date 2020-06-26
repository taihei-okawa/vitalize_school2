package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.MstUser;
import com.example.demo.repository.ClientRepository;
import com.example.demo.searchform.ClientSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  public List<Client> findAll() {
    return clientRepository.findAll();
  }

  public Client findOne(Long id) {
    return clientRepository.findById(id).orElse(null);
  }

  public Client save(Client client) {
    return clientRepository.save(client);
  }

  public void delete(Long id) {
    clientRepository.deleteById(id);
  }

  // 取引履歴機能の内容とページネーションを全検索
  public Page<Client> getAll(Pageable pageable, ClientSearchForm searchForm) {
    Specification<Client> spec = Specification
            .where(userIdEqual(searchForm.getId()))
            .and(nameContains(searchForm.getClientName()))
            .and(nameKanaContains(searchForm.getClientNameKana()));
    return clientRepository.findAll(spec, pageable);
  }

  /**
   *  ID検索
   */
  private static Specification<Client> userIdEqual(String id) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return id == "" || Objects.isNull(id) ? null : (root, query, cb) -> {
      return cb.equal(root.get("id"),  id);
    };
  }

  /**
   *  クライアント名検索
   */
  private static Specification<Client> nameContains(String clientName) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return clientName == "" || Objects.isNull(clientName) ? null : (root, query, cb) -> {
      return cb.like(root.get("clientName"), "%" + clientName + "%");
    };
  }

  /**
   *  クライアント名検索
   */
  private static Specification<Client> nameKanaContains(String clientNameKana) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return clientNameKana == "" || Objects.isNull(clientNameKana) ? null : (root, query, cb) -> {
      return cb.like(root.get("clientNameKana"), "%" + clientNameKana + "%");
    };
  }
}
