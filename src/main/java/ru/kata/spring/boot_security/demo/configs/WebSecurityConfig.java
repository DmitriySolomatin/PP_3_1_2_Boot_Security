package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;
    private final UserService userService;

    private final AgeFilter ageFilter;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImp userService, AgeFilter ageFilter) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
        this.ageFilter = ageFilter;
    }


    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                csrf().disable().addFilterAfter(ageFilter, FilterSecurityInterceptor.class)
                //Запрашивать авторизацию
                .authorizeRequests()
                //Доступ открыт всем
                .antMatchers("/", "/index", "/registration", "/api/registration", "/css/**", "/js/**").permitAll()
                //Если есть роль
                .antMatchers("/api/current", "/api/adult").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin", "/admin/**", "/api/**").hasRole("ADMIN")
                .antMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")
                //Все остальные страницы требуют авторизацию
                .anyRequest().authenticated()
                .and()
                //Разрешить доступ к форме логина для всех и применить хендлер
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                //Логаут разрешен всем
                .logout()
                .permitAll();

        return http.build();
    }

}