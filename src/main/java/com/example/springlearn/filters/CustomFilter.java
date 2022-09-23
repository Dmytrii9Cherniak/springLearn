package com.example.springlearn.filters;

import com.example.springlearn.dao.CustomerDao;
import com.example.springlearn.models.Customer;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class CustomFilter extends OncePerRequestFilter {

    private CustomerDao customerDao;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.replace("Bearer ", "");
            String subject = Jwts.parser()
                    .setSigningKey("key".getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            Customer customerByLogin = customerDao.findCustomerByLogin(subject);
            if (customerByLogin != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                    customerByLogin.getLogin(),
                                    customerByLogin.getPassword(),
                                    Collections.singletonList(new SimpleGrantedAuthority(customerByLogin.getRole()))
                                )
                        );
            }
        }
        filterChain.doFilter(request, response);

    }
}
