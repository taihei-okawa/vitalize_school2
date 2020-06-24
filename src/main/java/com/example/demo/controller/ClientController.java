package com.example.demo.controller;

import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;

@Controller
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientService clientService;

    @ModelAttribute
    Client setUpForm() {
        return new Client();
    }

	/** to 顧客 一覧画面表示*/
    @GetMapping(value = "/list")
    public String list(Model model) {
        List<Client> clientlist = clientService.findAll();
        model.addAttribute("clientlist", clientlist);
        return "client/list";
    }

	/** to 顧客 登録画面表示*/
    @GetMapping(value = "/add")
    public String add(Model model) {
        model.addAttribute("client", new Client());
        return "client/add";
    }

	/** to 顧客 編集画面表示*/
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	Client client = clientService.findOne(id);
        model.addAttribute("client", client);
        return "client/edit";
    }

	/** to 顧客 詳細画面表示*/
    @GetMapping(value = "{id}")
    public String view(@PathVariable Long id, Model model) {
        Client client = clientService.findOne(id);
        model.addAttribute("client", client);
        return "client/view";
    }

	/** to 顧客 process 登録*/
    @PostMapping(value = "/add")
    public String create(@ModelAttribute Client client) {
    	client.setInsertUserId(9001);
    	client.setUpdateUserId(9001);
    	clientService.save(client);
        Long newId = client.getId();
        return "redirect:/client/"+ newId;
    }

	/** to 顧客 process 編集*/
    @PostMapping(value = "/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Client client) {
        client.setInsertUserId(9001);
    	client.setUpdateUserId(9001);
        clientService.save(client);
        return "redirect:/client/"+"{id}";
    }

    /** to 顧客 削除*/
    @PostMapping("{id}")
    public String destroy(@PathVariable Long id) {
    	clientService.delete(id);
        return "redirect:/client/list";
    }

}