package com.example.spring_proj_completo.security;

import com.example.spring_proj_completo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/actuator").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("logout"))
                .and()
                .formLogin()
                .and()
                .httpBasic();

        //http.csrf().disable().cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info(delegatingPasswordEncoder.encode("novaSenha"));

        /*auth.inMemoryAuthentication()
                .withUser("giovane")
                .password(passwordEncoder.encode("8718"))
                .roles("USER", "ADMIN");

        auth.inMemoryAuthentication()
                .withUser("rizia")
                .password(passwordEncoder.encode("pim"))
                .roles("USER" );*/

        auth.userDetailsService(this.service).passwordEncoder(delegatingPasswordEncoder);

    }
}
