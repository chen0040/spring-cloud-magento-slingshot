package com.github.chen0040.eureka.web.configs;

import com.github.chen0040.eureka.web.api.AccountApi;
import com.github.chen0040.eureka.web.components.MagentoAuthenticationFilter;
import com.github.chen0040.eureka.web.components.SpringAuthenticationSuccessHandler;
import com.github.chen0040.eureka.web.services.SpringUserDetailService;
import com.github.chen0040.eureka.web.services.SpringUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Created by xschen on 15/10/2016.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   @Autowired
   private SpringUserDetailService userDetailService;


   @Autowired
   private SpringAuthenticationSuccessHandler authenticationSuccessHandler;

   @Autowired
   private AccountApi accountApi;

   @Autowired
   private SpringUserService userService;

   @Autowired
   private MagentoAuthenticationFilter magentoAuthenticationFilter;


   @Override
   protected void configure(HttpSecurity http) throws Exception {




      http
              .authorizeRequests()

              .antMatchers("/js/client/**").hasAnyRole("USER", "ADMIN")
              .antMatchers("/js/admin/**").hasAnyRole("ADMIN")
              .antMatchers("/admin/**").hasAnyRole("ADMIN")
              .antMatchers("/html/shared/**").hasAnyRole("USER", "ADMIN")
              .antMatchers("/html/admin/**").hasAnyRole("USER", "ADMIN")
              .antMatchers("/html/public/**").permitAll()
              .antMatchers("/js/commons/**").permitAll()
              .antMatchers("/sbs/**").permitAll()
              .antMatchers("/img/**").permitAll()
              .antMatchers("/css/**").permitAll()
              .antMatchers("/jslib/**").permitAll()
              .antMatchers("/webjars/**").permitAll()
              .antMatchers("/magento/**").permitAll()
              .antMatchers("/").permitAll()
              .antMatchers("/home").permitAll()
              .antMatchers("/bundle/**").permitAll()
              .antMatchers("/fonts/*.*").permitAll()
              .antMatchers("/signup").permitAll()
              .antMatchers("/locales").permitAll()
              .antMatchers("/locales/**").permitAll()
              .antMatchers("/privacy-policy").permitAll()
              .antMatchers("/change-locale").permitAll()
              .antMatchers("/link-cache").permitAll()
              .antMatchers("/signup-success").permitAll()
              .antMatchers("/terms-of-use").permitAll()
              .antMatchers("/contact-us").permitAll()
              .antMatchers("/forgot-password").permitAll()
              .antMatchers("/about-us").permitAll()
              .antMatchers("/kill").permitAll()
              .anyRequest().authenticated()
              .and()
              .formLogin()
              .loginPage("/login")
              .defaultSuccessUrl("/home")
              .successHandler(authenticationSuccessHandler)
              .permitAll()
              .and()
              .logout()
              .permitAll()
              .and()
              .addFilterBefore(magentoAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
              .csrf()
              .disable();
              //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
   }


   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {


      auth.userDetailsService(userDetailService)
              .passwordEncoder(new BCryptPasswordEncoder());
   }
}
