package vitalize.school.bank.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;

public class BaseController {
  public void checkAuth(LoginUser loginuser, String auth_code) throws AuthException {
    if (!loginuser.checkAuth(auth_code) && !loginuser.checkAuth("ADMIN")){
      throw new AuthException();
    }
  }

  @ExceptionHandler(AuthException.class)
  public String handleOriginalWebException(AuthException exception) {
    return "auth_error.html";
  }
}