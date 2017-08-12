package no.kantega.example.application.dataaccess;


import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import no.kantega.example.application.domain.Product;

public interface ProductDao {

    boolean deleteProduct(String id);

    @Transactional(readOnly = true)
    Optional<Product> getProduct(String id);

    @Transactional(readOnly = true)
    List<Product> getAllProducts();

    boolean persist(Product systemConfig);

    @Transactional(readOnly = true)
    int getNumberOfRows();
}
