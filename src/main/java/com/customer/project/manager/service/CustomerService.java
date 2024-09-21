package com.customer.project.manager.service;

import com.customer.project.manager.payload.customer.CustomerRequest;
import com.customer.project.manager.payload.customer.CustomerResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(UUID id, CustomerRequest request);
    CustomerResponse findById(UUID id);
    Page<CustomerResponse> findAll(Pageable pageable);
    void delete(UUID id);
}
