package no.kantega.example.application.health.db;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

import javax.management.ObjectName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.kantega.example.application.dataaccess.CustomerDao;
import no.kantega.example.application.dataaccess.ProductDao;
import no.kantega.example.application.health.HealthStatus;

/**
 * Created by marska on 11.04.2017.
 */
public class DbHealth implements DbHealthMBean {
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private ProductDao productDao;


    public DbHealth() {
        try {
            ManagementFactory.getPlatformMBeanServer().registerMBean(this,
                    new ObjectName(getClass().getPackage().getName(), "service", "Health"));//<-- Convention for health plugin
        } catch (Throwable ignore) {
        }
    }
    @Override
    public List<HealthStatus> healthList() throws Exception {
        return Arrays.asList(customerDaoStatus(), productDaoStatus());
    }
    private HealthStatus productDaoStatus() {
        try {
            long productCount = productDao.countProducts();
            return new HealthStatus("productDao", "INFO", "all good, a total of " + productCount + " products", "data access layer", 1.0);
        } catch(Exception e) {
            return new HealthStatus("productDao", "WARNING", e.getMessage(), "data access layer", 0.0);
        }
    }
    private HealthStatus customerDaoStatus() {
        try {
            long customerCount = customerDao.countCustomers();
            return new HealthStatus("customerDao", "INFO", "all good, a total of " + customerCount + " customers", "data access layer", 1.0);
        } catch(Exception e) {
            return new HealthStatus("customerDao", "WARNING", e.getMessage(), "data access layer", 0.0);
        }
    }
    @Override
    public String getCurrentStatus() {
        return "Good";
    }
}
