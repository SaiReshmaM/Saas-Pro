package com.saas.backend.controller;

import com.saas.backend.entity.Customer;
import com.saas.backend.entity.User;
import com.saas.backend.repository.CustomerRepository;
import com.saas.backend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private UserRepository userRepo;

    
    private User getUserFromRequest(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");

        System.out.println("EMAIL FROM REQUEST: " + email);

        if (email == null) {
            throw new RuntimeException("JWT missing or invalid");
        }

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //  CREATE
    @PostMapping
    public Customer create(@RequestBody Customer customer,
                           HttpServletRequest request) {

        User user = getUserFromRequest(request);

        customer.setTenantId(user.getTenantId());

        return repo.save(customer);
    }

    //  READ
    @GetMapping
    public List<Customer> getAll(HttpServletRequest request) {

        User user = getUserFromRequest(request);

        return repo.findByTenantId(user.getTenantId());
    }

    //  UPDATE (ADMIN only)
    @PutMapping("/{id}")
public Customer update(@PathVariable Long id,
                       @RequestBody Customer updatedCustomer,
                       HttpServletRequest request) {

    User user = getUserFromRequest(request);

    //  ROLE CHECK
    if (user.getRole() == null || !user.getRole().equalsIgnoreCase("ADMIN")) {
        throw new RuntimeException("Access Denied: Only ADMIN can update");
    }

    Customer existing = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    //  TENANT CHECK (VERY IMPORTANT)
    if (!existing.getTenantId().equals(user.getTenantId())) {
        throw new RuntimeException("Access Denied: Different tenant");
    }

    //  NULL SAFE UPDATE
    if (updatedCustomer.getName() != null) {
        existing.setName(updatedCustomer.getName());
    }

    if (updatedCustomer.getEmail() != null) {
        existing.setEmail(updatedCustomer.getEmail());
    }

    return repo.save(existing);
}

    //  DELETE (ADMIN only)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       HttpServletRequest request) {

        User user = getUserFromRequest(request);

        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Access Denied: Only ADMIN can delete");
        }

        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getTenantId().equals(user.getTenantId())) {
            throw new RuntimeException("Access Denied: Different tenant");
        }

        repo.deleteById(id);
    }
}