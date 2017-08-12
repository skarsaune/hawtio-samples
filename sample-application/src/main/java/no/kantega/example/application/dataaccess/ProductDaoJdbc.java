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

import no.kantega.example.application.domain.Product;

/**
 * Data layer access for objects
 */
@Repository
@Transactional
public class ProductDaoJdbc implements ProductDao {
    private static final String WHERE_ID = " where id = ?";

    private static final String SELECT_ALL = "select * from product";

    private static final String SELECT_SYSTEM = SELECT_ALL + WHERE_ID;

    private static final String INSERT_SYSTEM = "insert into product(id,name,price)"
            + "values(?,?,?)";

    private static final String UPDATE_SYSTEM = "update product set name = ?, price = ?" + WHERE_ID;


    private static final String DELETE_SYSTEM = "delete from system_config"+ WHERE_ID;


    private static final String COUNT_ROWS = "select count (id) as rowcount from system_config";

    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean deleteProduct(String id) {
        return getJdbcTemplate().update(DELETE_SYSTEM, id) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProduct(String id) {
        try {
            Product systemConfig = getJdbcTemplate().queryForObject(SELECT_SYSTEM, productRowMapper(), id);

            return Optional.of(systemConfig);

        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        try {
            return  getJdbcTemplate().query(SELECT_ALL, productRowMapper());
        } catch (EmptyResultDataAccessException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean persist(Product product) {
        Optional<Product> existingId = getProduct(product.getId());
        if (existingId.isPresent()) {
            return getJdbcTemplate()
                    .update(UPDATE_SYSTEM,
                            product.getName(),
                            product.getPrice(),
                            product.getId()) == 1;
        } else {
            return getJdbcTemplate().update(INSERT_SYSTEM,
                    product.getId(),
                    product.getName(),
                    product.getPrice()) == 1;
        }
    }


    @Override
    public int getNumberOfRows() {
        try {
            return getJdbcTemplate().queryForObject(COUNT_ROWS, Integer.class);
        } catch (Exception ignored) {
            return -1;
        }
    }

    private RowMapper<Product> productRowMapper() {
        return (resultSet, i) -> {
            return new Product(resultSet.getString("id"), resultSet.getString("name"), resultSet.getBigDecimal("price"));

        };
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
