package com.example.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.MstAuthRepository;
import com.example.demo.entity.MstAuth;

/**
 * 権限一覧情報 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class MstAuthService {

/**
* 権限情報 Repository
*/
@Autowired
private MstAuthRepository mstAuthRepository;
    // 権限の内容を全検索
    public List<MstAuth> searchAll() {
        return mstAuthRepository.findAll();
    }

    public MstAuth findOne(Long id) {
        return mstAuthRepository.findById(id).orElse(null);
    }


}