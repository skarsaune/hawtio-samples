package no.kantega.example.application.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import no.kantega.example.application.dataaccess.ProductDao;
import no.kantega.example.application.domain.Product;

/**
 * CRUD operations for a system configuration. The json-format used is defined in {@link SystemConfig}
 * A system configuration contains a specific id. And a list of containing properties (urls, username/password etc).
 * A system configuration is connected to a given {@link SystemType} A system configuration is typically refered to from a netowner-config.
 * A typical use case is that different netowners are using different instances of QC and KIS-systems.
 */
@EnableWebMvc
public class ProductController {
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductDao productDao;

    @Autowired
    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @RequestMapping(value="/products", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(getProductDao().getAllProducts());
    }

    @RequestMapping(value="/product/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@PathVariable("id") String id) {
        Optional<Product> systemConfig = getProductDao().getProduct(id);
        if (!systemConfig.isPresent()) {
            logger.info("Product not found for system id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(systemConfig.get());
    }

    @RequestMapping(value="/product", method = RequestMethod.PUT)
    public ResponseEntity<?> saveSystem(@RequestBody final Product  product ) {
        if(!hasValidId(product)) {
            return ResponseEntity.badRequest().build();
        } else {
            getProductDao().persist(product);
            return ResponseEntity.ok(product);            
        }
    }

    @RequestMapping(value="/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        if (!getProductDao().deleteProduct(id)) {
            logger.info("Product not deleted for id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Simple validator to prevent saving garbage
     */
    private boolean hasValidId(final Product product) {
        return product.getId() != null;
    }

    public ProductDao getProductDao() {
        return productDao;
    }
}
