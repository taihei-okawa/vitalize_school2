package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  public List<Client> findAll() {
    return clientRepository.findAll();
  }

  public List<Client> search(Integer clientId, Integer cliantName, String cliantNameKana) {
    List<Client> result = clientRepository.findAll();
    return result;
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
  public Page<Client> getAll(Pageable pageable) {
    return clientRepository.findAll(pageable);
  }
}
