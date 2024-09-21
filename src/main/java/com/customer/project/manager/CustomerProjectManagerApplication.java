package com.customer.project.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan("com.customer.project.manager.domain")
@ConfigurationPropertiesScan(basePackages = {"com.customer.project.manager.*"})
@EnableJpaRepositories(basePackages = {"com.customer.project.manager.*"})
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CustomerProjectManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerProjectManagerApplication.class, args);
    }
}
