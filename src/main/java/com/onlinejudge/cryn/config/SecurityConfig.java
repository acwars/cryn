package com.onlinejudge.cryn.config;

import com.onlinejudge.cryn.common.RoleEnum;
import com.onlinejudge.cryn.common.URIConst;
import com.onlinejudge.cryn.config.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private AuthenticationFailureHandler loginFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncode() {
        return new BCryptPasswordEncoder();
    }


    /**
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncode());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new ValidateCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/user/loginPage")
                .loginProcessingUrl("/user/loginProcess")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .logout().logoutUrl("/user/logout").logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(3600 * 24)
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
                //??????session?????????????????????
//                .invalidSessionUrl()
                //????????????????????????Session
                .maximumSessions(1)
                //???????????????????????????Session????????????????????????????????????????????????????????????
//                .maxSessionsPreventsLogin(true)
                //????????????????????????session?????????????????????
//                .expiredSessionStrategy()
                .and()
                .and()
                .authorizeRequests()
                .antMatchers(URIConst.ALLOWED_URI).permitAll()
                .antMatchers(URIConst.NOT_ALLOWED_URI)
                .hasRole(RoleEnum.ADMIN.getConfigName())
                .anyRequest()
                .permitAll()
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable();

    }

}
