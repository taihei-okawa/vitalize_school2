//package com.example.securityweb;
//
//import com.example.demo.entity.MstUser;
//import com.example.demo.service.MstUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//  @Autowired
//  private MstUserService mstUserService;
//  @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//          .antMatchers("/login", "/login-error").permitAll()
//          .antMatchers("/**").hasRole("USER")
//          .and()
//          .formLogin()
//          .loginPage("/login").failureUrl("/login-error");
//  }
//  //変更点 ロード時に、「admin」ユーザを登録する。
//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth
//      .userDetailsService(mstUserService)
//      .passwordEncoder(passwordEncoder());
//    //TODO: propertyでadmin情報は管理しましょう。
//    userService.registerAdmin("admin", "secret", "admin@localhost");
//  }
//
//  //変更点 PasswordEncoder(BCryptPasswordEncoder)メソッド
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    //
//    return new BCryptPasswordEncoder();
//  }
//
//}