package com.example.springlearn.controllers;

import com.example.springlearn.dto.CustomerDTO;
import com.example.springlearn.models.Customer;
import com.example.springlearn.service.CustomerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class MainController {

    private CustomerService customerService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public void register (@RequestBody CustomerDTO customerDTO) {
        customerService.save(customerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody CustomerDTO customerDTO) {

        Authentication authenticaticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customerDTO.getLogin(), customerDTO.getPassword()
                ));
        if (authenticaticate != null) {

            String jwtToken = Jwts.builder()
                    .setSubject(authenticaticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "key".getBytes())
                    .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .compact();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken);
            return new ResponseEntity<>("You are logged in", headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("Ban credentials", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/secure")
    public List <Customer> getSecuredCustomers() {
        return customerService.findAll();
    }

}
