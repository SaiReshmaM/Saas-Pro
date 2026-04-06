
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
@RequestMapping("/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceRepository repo;

    @Autowired
    private UserRepository userRepo;

    // ✅ CREATE INVOICE
    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only ADMIN can create invoice");
        }

        invoice.setTenantId(user.getTenantId());

        return repo.save(invoice);
    }

    // ✅ GET ALL INVOICES (Tenant-based)
    @GetMapping
    public List<Invoice> getAll() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        return repo.findByTenantId(user.getTenantId());
    }

    // 🔒 UPDATE INVOICE (ADMIN only)
    @PutMapping("/{id}")
    public Invoice update(@PathVariable Long id, @RequestBody Invoice updated) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only ADMIN can update invoice");
        }

        Invoice existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        existing.setAmount(updated.getAmount());
        existing.setStatus(updated.getStatus());

        return repo.save(existing);
    }

    // 🔒 DELETE INVOICE (ADMIN only)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only ADMIN can delete invoice");
        }

        repo.deleteById(id);
    }

    // 🚀 NEW: INVOICE SUMMARY API
    @GetMapping("/summary")
    public String summary() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        List<Invoice> invoices = repo.findByTenantId(user.getTenantId());

        int total = invoices.size();

        long paid = invoices.stream()
                .filter(i -> "PAID".equalsIgnoreCase(i.getStatus()))
                .count();

        return "Total Invoices: " + total + ", Paid Invoices: " + paid;
    }
}