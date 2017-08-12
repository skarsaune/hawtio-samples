package no.kantega.example.application.dataaccess;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import no.kantega.example.application.domain.Customer;

/**
 * Handles database operations for customer
 */
@Repository
@Transactional
public class CustomerDaoJdbc implements CustomerDao {

    private static final String WHERE_CLAUSE = " where identifier = ?";

    private static final String SELECT_ALL = "select * from customer";

    private static final String SELECT_SINGLE_CUSTOMER = SELECT_ALL + WHERE_CLAUSE;

    private static final String INSERT_CUSTOMER = "insert into customer(identifier,given_name,family_name)"
            + "values(?,?,?)";

    private static final String UPDATE_CUSTOMER = "update customer set given_name = ? , family_name = ?" + WHERE_CLAUSE;

    private static final String DELETE_CUSTOMER = "delete from customer "+WHERE_CLAUSE;

    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean deleteCustomer(String identifier) {
        return getJdbcTemplate().update(DELETE_CUSTOMER, identifier) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomer(String identifier) {
        try {
            return Optional.of(getJdbcTemplate().queryForObject(SELECT_SINGLE_CUSTOMER, customerRowMapper(), identifier));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    private RowMapper<Customer> customerRowMapper() {
        return (resultSet, i) -> {
            return new Customer(resultSet.getString("identifier"), resultSet.getString("given_name"), resultSet.getString("family_name"));
        };
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        try {
            return getJdbcTemplate().query(SELECT_ALL, customerRowMapper());
        } catch (EmptyResultDataAccessException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean persist(Customer customer) {
        Optional<Customer> existingId = getCustomer(customer.getIdentifier());
        if (existingId.isPresent()) {
            return getJdbcTemplate()
                    .update(UPDATE_CUSTOMER,
                            customer.getGivenName(),
                            customer.getFamilyName(),
                            customer.getIdentifier()) == 1;
        } else {
            return getJdbcTemplate().update(INSERT_CUSTOMER,
                    customer.getIdentifier(),
                    customer.getGivenName(),
                    customer.getFamilyName()) == 1;
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public long countCustomers() {
        return jdbcTemplate.queryForObject("select count * from customer", Long.class);
        
    }

}
