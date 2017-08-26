package no.kantega.example.application.health;

import java.util.List;

/**
 * Returns the status events of the application
 */
public interface HealthMBean {

    List<HealthStatus> healthList() throws Exception;

    String getCurrentStatus();
}
