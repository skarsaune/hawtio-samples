package no.kantega.example.application.dataaccess;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import no.kantega.example.application.domain.Customer;

public interface CustomerDao {

    boolean deleteCustomer(String id);

    @Transactional(readOnly = true)
    Optional<Customer> getCustomer(String gln);

    @Transactional(readOnly = true)
    List<Customer> getAllCustomers();

    boolean persist(Customer config);

    long countCustomers();
}
