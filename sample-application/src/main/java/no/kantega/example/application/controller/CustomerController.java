package no.kantega.example.application.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import no.kantega.example.application.dataaccess.CustomerDao;
import no.kantega.example.application.domain.Customer;

/**
 * Contains CRUD operations for customers
 */
@EnableWebMvc
@Controller
public class CustomerController {
    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerDao customerDao;

    @Autowired
    public CustomerController(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> getAllNetowners() {
        return ResponseEntity.ok(getCustomerDao().getAllCustomers());

    }

    @RequestMapping(value="/customer/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("identifier") String identifier) {
        Optional<Customer> customerOptional = getCustomerDao().getCustomer(identifier);
        if (!customerOptional.isPresent()) {
            logger.info("Customer not found for identifier {}", identifier);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerOptional.get());
    }

    @RequestMapping(value="/customer", method = RequestMethod.PUT)
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        if (isEmpty(customer.getIdentifier()) || isEmpty(customer.getFamilyName())) {
            logger.info("Bad request returned for customer with identifier {} and family_name {}", customer.getIdentifier(),
                    customer.getFamilyName());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getCustomerDao().persist(customer));
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    @RequestMapping(value="/customer/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteConfig(@PathVariable("id") String id) {
        if (!getCustomerDao().deleteCustomer(id)) {
            logger.info("Customer data not deleted for id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

}
