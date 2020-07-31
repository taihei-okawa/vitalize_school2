package vitalize.school.bank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vitalize.school.bank.LoginUser;
import vitalize.school.bank.entity.MstUser;
import vitalize.school.bank.repository.MstUserRepository;

import java.util.Optional;

@Service
@Slf4j
public class LoginUserDetailsService implements UserDetailsService {
  private final MstUserRepository userRepository;

  public LoginUserDetailsService(MstUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
    assert(userCode != null);
    log.debug("loadUserByUsername(userCode):[{}]", userCode);

    Optional<MstUser> user = userRepository.findByUserNameAndDeleteDateIsNull(userCode);
    return user
      .map(LoginUser::new)
      .orElseThrow(() -> new UsernameNotFoundException("User not found by userCode:[" + userCode + "]"));
  }
}
