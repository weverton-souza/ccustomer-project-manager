package com.customer.project.manager.service.impl;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.exception.EntityNotFoundException;
import com.customer.project.manager.payload.customer.CustomerRequest;
import com.customer.project.manager.payload.customer.CustomerResponse;
import com.customer.project.manager.payload.customer.convert.CustomerConverter;
import com.customer.project.manager.repository.CustomerRepository;
import com.customer.project.manager.service.CustomerService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public CustomerResponse create(CustomerRequest request) {
        LOGGER.info("class=CustomerService, method=create, message=Creating a new customer");
        request.setPassword(this.bCryptPasswordEncoder.encode(request.getPassword()));
        Customer customer = CustomerConverter.convert(request);

        customer = this.customerRepository.save(customer);
        LOGGER.info("class=CustomerService, method=create, message=Customer created with id: {}", customer.getId());

        return CustomerConverter.convert(customer);
    }

    @Override
    public CustomerResponse update(UUID id, CustomerRequest request) {
        LOGGER.info("class=CustomerService, method=update, message=Updating customer with id: {}", id);
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        customer = this.customerRepository.save(customer);
        LOGGER.info("class=CustomerService, method=update, message=Customer updated with id: {}", customer.getId());

        return CustomerConverter.convert(customer);
    }

    @Override
    public CustomerResponse findById(UUID id) {
        LOGGER.info("class=CustomerService, method=findById, message=Finding customer with id: {}", id);
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        LOGGER.info("class=CustomerService, method=findById, message=Customer found with id: {}", customer.getId());

        return CustomerConverter.convert(customer);
    }

    @Override
    public Page<CustomerResponse> findAll(Pageable pageable) {
        LOGGER.info("class=CustomerService, method=findAll, message=Finding all customers");
        return this.customerRepository.findAll(pageable)
                .map(CustomerConverter::convert);
    }

    @Override
    public void delete(UUID id) {
        LOGGER.info("class=CustomerService, method=delete, message=Deleting customer with id: {}", id);
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        this.customerRepository.delete(customer);
        LOGGER.info("class=CustomerService, method=delete, message=Customer deleted with id: {}", id);
    }
}
