package no.kantega.example.application.domain;

import java.math.BigDecimal;

public class Product {
    
    private final String id;
    private final String name;
    private final BigDecimal price;
    public Product(String id, String name, BigDecimal price) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public BigDecimal getPrice() {
        return price;
    }
}
