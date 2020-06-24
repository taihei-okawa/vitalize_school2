package com.example.demo.service;

import com.example.demo.entity.MstUser;
import com.example.demo.repository.MstUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import static com.example.demo.service.MstUserSpecifications.*;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class MstUserService {

  @Autowired
  private MstUserRepository mstUserRepository;

  public List<MstUser> findUsers(Long id, String userName, Integer branchCode) {
    return mstUserRepository.findAll(Specification
            .where(MstUserSpecifications.userIdContains(id))
            .and(MstUserSpecifications.nameContains(userName))
            .and(MstUserSpecifications.branchCodeContains(branchCode))
    );
  }

  public List<MstUser> findAll() {
    return mstUserRepository.findAll();
  }


  public MstUser findOne(Long id) {
    return mstUserRepository.findById(id).orElse(null);
  }

  public MstUser save(MstUser mstUser) {
    return mstUserRepository.save(mstUser);
  }

//  public MstUser updateMstUser(MstUser mstUser) {
//    return mstUserRepository().save(mstUser);
//  }

  public void delete(Long id) {
    mstUserRepository.deleteById(id);
  }
}