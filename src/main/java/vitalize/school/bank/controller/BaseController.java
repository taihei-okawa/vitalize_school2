package vitalize.school.bank.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import vitalize.school.bank.AuthException;
import vitalize.school.bank.LoginUser;
import vitalize.school.bank.entity.BaseEntity;

import java.util.Date;

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

  public void insertEntity(BaseEntity entity, LoginUser loginUser){
    entity.setInsertUserId(loginUser.getUser().getId().intValue());
    entity.setInsertDate(new Date());
    entity.setUpdateUserId(loginUser.getUser().getId().intValue());
    entity.setUpdateDate(new Date());
  }

  public void updateEntity(BaseEntity entity, LoginUser loginUser){
    entity.setUpdateUserId(loginUser.getUser().getId().intValue());
    entity.setUpdateDate(new Date());
  }

  public void deleteEntity(BaseEntity entity, LoginUser loginUser){
    entity.setDeleteDate(new Date());
  }
}