package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MstFeeController {

	/** to 手数料マスタ機能 一覧*/
    @RequestMapping(value = "mst_fee/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("message", "Hello World!!");
        return "mst_fee/list";
    }

	/** to 手数料マスタ機能 詳細画面表示*/
    @RequestMapping(value = "mst_fee/view", method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("message", "Hello World!!");
        return "mst_fee/view";
    }

    /** to 手数料マスタ機能 削除*/

	/** to 手数料マスタ機能 登録画面表示*/
    @RequestMapping(value = "mst_fee/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("message", "Hello World!!");
        return "mst_fee/add";
    }

	/** to 手数料マスタ機能 編集画面表示*/
    @RequestMapping(value = "mst_fee/edit", method = RequestMethod.GET)
    public String edit(Model model) {
        model.addAttribute("message", "Hello World!!");
        return "mst_fee/edit";
    }

}