package no.kantega.example.application.controller;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import no.kantega.example.application.dataaccess.CustomerDao;

public class HealthcheckController {
    private static Logger logger = LoggerFactory.getLogger(HealthcheckController.class);
    @Autowired
    public HealthcheckController(CustomerDao repository){
    }

    public String healthCheck() {
        return String.format("Health at %s for gln-config,version %s, running since: %s",
                Instant.now(),
                getVersion(),
                getRunningSince());
    }


    private String getRunningSince() {
        long uptimeInMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        return Instant.now().minus(uptimeInMillis, ChronoUnit.MILLIS).toString();
    }

    private String getVersion() {
        Properties mavenProperties = new Properties();
        String resourcePath = "/META-INF/maven/no.kantega.examples/example-application/pom.properties";
        URL mavenVersionResource = this.getClass().getResource(resourcePath);
        if (mavenVersionResource != null) {
            try {
                mavenProperties.load(
                        mavenVersionResource.openStream());
                return mavenProperties.getProperty("version", "missing version info in " + resourcePath);
            } catch (IOException e) {
                logger.warn("Problem reading version resource from classpath: ", e);
            }
        }
        return "(DEV VERSION)";
    }
}
