package vitalize.school.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;
import vitalize.school.bank.entity.MstUser;
import vitalize.school.bank.searchform.MstUserSearchForm;
import vitalize.school.bank.service.MstUserService;

@Controller
@RequestMapping("/mst_user")
public class MstUserController extends BaseController {
  protected String AUTH_CODE = "USER";

  @Autowired
  private MstUserService mstUserService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  /**
   * to 社員 一覧画面表示
   */
  @GetMapping(value = "/list")
  public String displayList(Model model, @ModelAttribute MstUserSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable,
                            @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    Page<MstUser> mstUserList = mstUserService.getAll(pageable, searchForm);
    model.addAttribute("page", mstUserList);
    model.addAttribute("userList", mstUserList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "mst_user/list";
  }

  /**
   * to 社員 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model,
                    @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    //空のオブジェクトを渡す。th:object="${mstUser}"
    model.addAttribute("mstUser", new MstUser());
    return "/mst_user/add";
  }

  /**
   * to 社員 詳細画面表示
   */
  @GetMapping(value = "/{id}")
  public String view(@PathVariable Long id, Model model,
                     @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    MstUser mstUser = mstUserService.findOne(id);
    model.addAttribute("mstUser", mstUser);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "mst_user/view";
  }

  /**
   * to ユーザー process 登録
   */
  @PostMapping(value = "/add")
  public String create(RedirectAttributes attr, @Validated @ModelAttribute MstUser mstUser, BindingResult result,
                       @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    if (result.hasErrors()) {
      return "mst_user/add";
    }
    mstUser.setPassword(passwordEncoder.encode(mstUser.getPassword()));
    insertEntity(mstUser, loginUser);
    mstUser.setStatus(1);
    mstUserService.save(mstUser);
    Long newId = mstUser.getId();
    attr.addFlashAttribute("message", "※社員が作成されました※");
    return "redirect:/mst_user/" + newId;
  }

  /**
   * to 社員　更新、編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(RedirectAttributes attr,@PathVariable Long id, Model model,
                     @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    MstUser mstUser = mstUserService.findOne(id);
    model.addAttribute("mstUser", mstUser);
    attr.addFlashAttribute("message", "※社員が更新されました※");
    return "mst_user/edit";
  }

  /**
   * to 社員 process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(@Validated @ModelAttribute MstUser mstUser, BindingResult result, @PathVariable Long id,
                       @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    if(result.hasErrors()) return "mst_user/edit";
    mstUser.setPassword(passwordEncoder.encode(mstUser.getPassword()));
    updateEntity(mstUser, loginUser);
    mstUser.setStatus(1);
    mstUserService.save(mstUser);
    return "redirect:/mst_user/list";
  }

  /**
   * to 削除機能　社員一覧画面
   */
  @PostMapping("{id}")
  public String destroy(RedirectAttributes attr, @PathVariable Long id,
                        @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    MstUser mstUser = mstUserService.findOne(id);
    deleteEntity(mstUser, loginUser);
    mstUserService.save(mstUser);
    attr.addFlashAttribute("message", "※社員が削除されました※");
    return "redirect:/mst_user/list";
  }
}