package vitalize.school.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;
import vitalize.school.bank.entity.MstAuth;
import vitalize.school.bank.searchform.MstAuthSearchForm;
import vitalize.school.bank.service.MstAuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;

@Controller
@RequestMapping("/mst_auth")
public class MstAuthController extends BaseController {
  protected String AUTH_CODE = "AUTH";

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
  @GetMapping(value = "/list")
  public String displayList(Model model, @ModelAttribute MstAuthSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable,
                            @AuthenticationPrincipal LoginUser loginUser) throws AuthException {
    checkAuth(loginUser, AUTH_CODE);
    Page<MstAuth> mstAuthlist = mstAuthService.searchAll(pageable, searchForm);
    model.addAttribute("url", "list");
    model.addAttribute("mstAuthlist", mstAuthlist.getContent());
    return "mst_auth/list";
  }
}