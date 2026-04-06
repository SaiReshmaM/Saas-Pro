package com.saas.backend.controller;

import com.saas.backend.entity.Invoice;
import com.saas.backend.entity.User;
import com.saas.backend.repository.InvoiceRepository;
import com.saas.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/revenue")
    public Double getRevenue() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        List<Invoice> invoices = invoiceRepo.findByTenantId(user.getTenantId());

        double total = 0;

        for (Invoice i : invoices) {
            if ("PAID".equalsIgnoreCase(i.getStatus())) {
                total += i.getAmount();
            }
        }

        return total;
    }
}
// package com.saas.backend.controller;

// import com.saas.backend.entity.Invoice;
// import com.saas.backend.entity.User;
// import com.saas.backend.repository.InvoiceRepository;
// import com.saas.backend.repository.UserRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import org.springframework.security.core.context.SecurityContextHolder;

// import java.util.List;

// @RestController
// @RequestMapping("/dashboard")
// public class DashboardController {

//     @Autowired
//     private InvoiceRepository invoiceRepo;

//     @Autowired
//     private UserRepository userRepo;

//     @GetMapping("/revenue")
//     public Double getTotalRevenue() {

//         String email = SecurityContextHolder.getContext().getAuthentication().getName();
//         User user = userRepo.findByEmail(email).orElseThrow();

//         List<Invoice> invoices = invoiceRepo.findByTenantId(user.getTenantId());

//         double total = 0;

//         for (Invoice inv : invoices) {
//             if ("PAID".equalsIgnoreCase(inv.getStatus())) {
//                 total += inv.getAmount();
//             }
//         }

//         return total;
//     }
// }
