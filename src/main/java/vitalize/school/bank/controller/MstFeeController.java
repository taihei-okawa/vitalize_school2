package vitalize.school.bank.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vitalize.school.bank.searchform.MstFeeSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vitalize.school.bank.service.MstFeeService;
import vitalize.school.bank.entity.MstFee;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/mst_fee")
public class MstFeeController {

  @Autowired
  private MstFeeService mstFeeService;

  private static final int DEFAULT_PAGEABLE_SIZE = 15;

  /**
   * to 手数料マスタ 一覧画面表示
   * to 手数料マスタ ページネーション
   */
  @GetMapping(value = "/list")
  public String displayList(Model model, @ModelAttribute MstFeeSearchForm searchForm,
                            @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0)Pageable pageable) {
    Page<MstFee> mstFeeList = mstFeeService.getAll(pageable, searchForm);
    model.addAttribute("page", mstFeeList);
    model.addAttribute("mstFeeList", mstFeeList.getContent());
    model.addAttribute("url", "list");
    model.addAttribute("searchForm", searchForm);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "mst_fee/list";
  }

  /**
   * to 手数料マスタ 登録画面表示
   */
  @GetMapping(value = "/add")
  public String add(Model model) {
    model.addAttribute("mstFee", new MstFee());
    return "mst_fee/add";
  }

  /**
   * to 手数料マスタ 編集画面表示
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    MstFee mstFee = mstFeeService.findOne(id);
    model.addAttribute("mstFee", mstFee);
    return "mst_fee/edit";
  }

  /**
   * to 手数料マスタ 詳細画面表示
   */
  @GetMapping(value = "{id}")
  public String view(@PathVariable Long id, Model model) {
    MstFee mstFee = mstFeeService.findOne(id);
    model.addAttribute("mstFee", mstFee);
    String message = (String) model.getAttribute("message");
    model.addAttribute("redirectParameter", message);
    return "mst_fee/view";
  }

  /**
   * to 手数料マスタ process 登録
   */
  @PostMapping(value = "/add")
  public String create(RedirectAttributes attr, @Validated @ModelAttribute MstFee mstFee, BindingResult result) {
    if (result.hasErrors()) {
      return "mst_fee/add";
    }
    mstFee.setStartDay(mstFee.getStartDay().replace("T", "  "));
    mstFee.setEndDay(mstFee.getEndDay().replace("T", "  "));
    String strDay = mstFee.getStartDay();
    String endDay = mstFee.getEndDay();
    String time = ":00";
    String strTime = strDay.concat(time);
    String endTime = endDay.concat(time);
    mstFee.setStartDay(strTime);
    mstFee.setEndDay(endTime);
    mstFee.setInsertUserId(9001);
    mstFee.setUpdateUserId(9001);
    mstFeeService.save(mstFee);
    Long newId = mstFee.getId();
    attr.addFlashAttribute("message", "※手数料が作成されました※");
    return "redirect:/mst_fee/" + newId;
  }

  /**
   * to 手数料マスタ process 編集
   */
  @PostMapping(value = "/edit/{id}")
  public String update(RedirectAttributes attr,@Validated @ModelAttribute MstFee mstFee, BindingResult result,@PathVariable Long id) {
    if(result.hasErrors()) return "mst_fee/edit";
    mstFee.setInsertUserId(9001);
    mstFee.setUpdateUserId(9001);
    mstFee.setStartDay(mstFee.getStartDay().replace("T", "  "));
    mstFee.setEndDay(mstFee.getEndDay().replace("T", "  "));
    String strDay = mstFee.getStartDay();
    String endDay = mstFee.getEndDay();
    String time = ":00";
    String strTime = strDay.concat(time);
    String endTime = endDay.concat(time);
    mstFee.setStartDay(strTime);
    mstFee.setEndDay(endTime);
    mstFeeService.save(mstFee);
    attr.addFlashAttribute("message", "※手数料が更新されました※");
    return "redirect:/mst_fee/" + "{id}";
  }

  /**
   * to 手数料マスタ 削除
   */
  @PostMapping("{id}")
  public String destroy(RedirectAttributes attr,@PathVariable Long id) {
    mstFeeService.delete(id);
    attr.addFlashAttribute("message", "※手数料が削除されました※");
    return "redirect:/mst_fee/list";
  }

}