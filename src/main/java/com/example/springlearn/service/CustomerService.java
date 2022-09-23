package com.example.springlearn.service;

import com.example.springlearn.dao.CustomerDao;
import com.example.springlearn.dto.CustomerDTO;
import com.example.springlearn.models.Customer;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private PasswordEncoder passwordEncoder;
    private CustomerDao customerDao;

    public void save(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setLogin(customerDTO.getLogin());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerDao.save(customer);
    }

    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    public Customer findById(int id) {
        return customerDao.findById(id).orElseGet(Customer::new);
    }

    public Customer findCustomerByLogin(String customerLogin) {
        return customerDao.findCustomerByLogin(customerLogin);
    }

}