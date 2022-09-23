package com.example.springlearn.dao;


import com.example.springlearn.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer, Integer> {

    Customer findCustomerByLogin(String login);

}
