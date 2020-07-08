package vitalize.school.bank.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import vitalize.school.bank.entity.MstUser;
import vitalize.school.bank.searchform.MstUserSearchForm;
import vitalize.school.bank.service.MstUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mst_user")
public class MstUserController {

  @Autowired
  private MstUserService mstUserService;
  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  /**
   * to 社員 一覧画面表示
   */
  @GetMapping(value = "/list")
  public String displayList(Model model, @ModelAttribute MstUserSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable) {
    Page<MstUser> mstUserList = mstUserService.getAll(pageable, searchForm);
    model.addAttribute("page", mstUserList);
    model.addAttribute("userList", mstUserList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    return "mst_user/list";
  }

  /**
   * to 社員 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    //空のオブジェクトを渡す。th:object="${mstUser}"
    model.addAttribute("mstUser", new MstUser());
    return "/mst_user/add";
  }

  /**
   * to 社員 詳細画面表示
   */
  @GetMapping(value = "/{id}")
  public String view(@PathVariable Long id, Model model) {
    MstUser mstUser = mstUserService.findOne(id);
    model.addAttribute("mstUser", mstUser);
    return "mst_user/view";
  }

  /**
   * to ユーザー process 登録
   */
  @PostMapping(value = "/add")
  public String create(@Validated @ModelAttribute MstUser mstUser, BindingResult result) {
    if (result.hasErrors()) {
      return "mst_user/add";
    }
    mstUser.setInsertUserId(9001);
    mstUser.setUpdateUserId(9001);
    mstUser.setStatus(1);
    mstUserService.save(mstUser);
    Long newId = mstUser.getId();
    return "redirect:/mst_user/" + newId;
  }

  /**
   * to 社員　更新、編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    MstUser mstUser = mstUserService.findOne(id);
    model.addAttribute("mstUser", mstUser);
    //model.addAttribute("id", id);
    return "mst_user/edit";
  }

  /**
   * to 社員 process 編集
   */
  @PostMapping(value = "/edit/{id}") //PostMappingを使う
  public String update(@PathVariable Long id, @ModelAttribute MstUser mstUser) {
    mstUser.setInsertUserId(9001);
    mstUser.setUpdateUserId(9001);
    mstUser.setStatus(1);
    mstUserService.save(mstUser);
    return "redirect:/mst_user/list";
  }

  /**
   * to 削除機能　社員一覧画面
   */
  @PostMapping("{id}")
  public String destroy(@PathVariable Long id) {
    mstUserService.delete(id);
    return "redirect:/mst_user/list";
  }

}