package vitalize.school.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

  @GetMapping(path = "login")
  public String login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "logout", required = false) String logout,
                      Model model, HttpSession session) {

    model.addAttribute("showErrorMsg", false);
    model.addAttribute("showLogoutedMsg", false);
    if (error != null) {
      model.addAttribute("showErrorMsg", true);
    } else if (logout != null) {
      model.addAttribute("showLogoutedMsg", true);
    }
    return "login";
  }
}