package vitalize.school.bank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import vitalize.school.bank.entity.MstAuth;
import vitalize.school.bank.entity.MstUser;

@Slf4j
public class LoginUser extends org.springframework.security.core.userdetails.User{
  private MstUser user;

  public LoginUser(MstUser user) {
    super(user.getUserName(), user.getPassword(), convertGrantedAuthorities(user.getMstAuths()));
    this.user = user;
  }

  public MstUser getUser() {
    return user;
  }

  static Set<GrantedAuthority> convertGrantedAuthorities(List<MstAuth> auths) {
    if (auths == null) return Collections.emptySet();
    Set<GrantedAuthority> authorities = auths.stream()
      .map(auth -> new SimpleGrantedAuthority(auth.getAuthCode()))
      .collect(Collectors.toSet());
    return authorities;
  }

  public boolean checkAuth(String auth_code){
    return user.getMstAuths().stream().map(a -> a.getAuthCode()).collect(Collectors.toList()).contains(auth_code);
  }
}
