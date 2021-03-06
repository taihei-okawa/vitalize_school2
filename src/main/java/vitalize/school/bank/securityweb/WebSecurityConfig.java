package vitalize.school.bank.securityweb;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//  // アカウント登録時のパスワードエンコードで利用するためDI管理する。
//  @Bean
//  PasswordEncoder passwordEncoder() {
//    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//  }

  public void configure(WebSecurity web) throws Exception {
    // @formatter:off
    web
      .debug(false)
      .ignoring()
      .antMatchers("/images/**", "/js/**", "/css/**")
    ;
    // @formatter:on
  }

  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off ~ @formatter:onではさまれているところはソースの自動整形からはずすことが可能
    // @formatter:off
    http
      .authorizeRequests()
      .mvcMatchers("/login").permitAll() // ログインしていなくてもみれるページの指定
      .anyRequest().authenticated().and() // 全てのページはログインしているユーザーしか見れない
      .formLogin()
        .loginPage("/login").permitAll()
        .defaultSuccessUrl("/").and() //ログイン成功後に開くページの指定
      .logout() //ログアウト時の処理
      .invalidateHttpSession(true) //セッションの無効化
      .deleteCookies("JSESSIONID") //cookieから「JSESSIONID」を削除する
      .logoutSuccessUrl("/login") //ログアウト成功後に開くページの指定
    ;
    // @formatter:on
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
//  DB参照ではなく、インメモリを参照してログインしたい場合に使用
//  要はDBのユーザー無視で、ここに書いてあるIDとパスワードでログインを可能にする
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    auth.inMemoryAuthentication()
//      .withUser("user").password(encoder.encode("password")).roles("USER")
//      .and()
//      .withUser("admin").password(encoder.encode("adminpassword")).roles("ADMIN");
//  }
}