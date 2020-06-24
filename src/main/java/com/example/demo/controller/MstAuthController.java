package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.demo.entity.MstAuth;
import com.example.demo.service.MstAuthService;

@Controller
public class MstAuthController {

/**
 * 権限情報 Service
 */
@Autowired
private MstAuthService mstAuthService;

    /** to 権限機能 一覧*/
    @GetMapping(value = "/mst_auth/list")
    public String displayList(Model model) {
        List<MstAuth> mstAuthlist = mstAuthService.searchAll();
        model.addAttribute("mstAuthlist", mstAuthlist);
        return "mst_auth/list";
    }

    /** to 権限機能 詳細画面表示*/
    @GetMapping(value = "/mst_auth/{id}")
    public String view(@PathVariable Long id, Model model) {
        MstAuth mstAuth = mstAuthService.findOne(id);
        model.addAttribute("mstAuth", mstAuth);
        return "mst_auth/view";
    }


}

