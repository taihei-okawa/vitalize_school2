package com.example.demo.controller;

import com.example.demo.entity.MstAuth;
import com.example.demo.service.MstAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MstAuthController {

  /**
   * 権限情報 Service
   */
  @Autowired
  private MstAuthService mstAuthService;
  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  /**
   * to 権限機能 一覧画面表示
   * to 権限機能 ページネーション
   */
  @GetMapping(value = "/mst_auth/list")
  public String displayList(Model model, @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable) {
    Page<MstAuth> mstAuthlist = mstAuthService.getAll(pageable);
    model.addAttribute("page", mstAuthlist);
    model.addAttribute("mstAuthlist", mstAuthlist.getContent());
    model.addAttribute("url", "list");

    return "mst_auth/list";
  }

  /**
   * to 権限機能 詳細画面表示
   */
  @GetMapping(value = "/mst_auth/{id}")
  public String view(@PathVariable Long id, Model model) {
    MstAuth mstAuth = mstAuthService.findOne(id);
    model.addAttribute("mstAuth", mstAuth);
    return "mst_auth/view";
  }

}

