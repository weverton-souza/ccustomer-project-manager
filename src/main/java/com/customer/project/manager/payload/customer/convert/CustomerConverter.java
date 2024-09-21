package com.customer.project.manager.payload.customer.convert;

import com.customer.project.manager.domain.Customer;
import com.customer.project.manager.payload.customer.CustomerRequest;
import com.customer.project.manager.payload.customer.CustomerResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerConverter {

    public static Customer convert(CustomerRequest request) {
        return Customer
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .build();
    }

    public static CustomerResponse convert(Customer customer) {
        return new CustomerResponse()
                .setId(customer.getId())
                .setName(customer.getName())
                .setEmail(customer.getEmail())
                .setPhone(customer.getPhone());
    }
}
