package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.kata.spring.boot_security.demo.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AgeFilter extends GenericFilterBean {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (((HttpServletRequest) request).getRequestURI().equals("/adult")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                User user = (User) authentication.getPrincipal();
                if (user.getAge() >= 18) {
                    ((HttpServletResponse) response).sendRedirect("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley");
                } else {
                    ((HttpServletResponse) response).sendRedirect("https://www.smeshariki.ru/");
                }
            }
        } else {
            chain.doFilter(request, response);
        }
        ;

    }
}
