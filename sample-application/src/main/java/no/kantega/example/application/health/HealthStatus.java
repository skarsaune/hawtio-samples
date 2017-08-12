package no.kantega.example.application.health;

import java.io.Serializable;

public class HealthStatus implements Serializable {

    private static final long serialVersionUID = -5084785872287783327L;
    private final String healthId;
    private final String level;
    private final String message;
    private final double healthPercent;

    public HealthStatus(String healthId, String level, String message, String resource, double percentOk) {
        this.healthId = healthId;
        this.level = level;
        this.message = message;
        this.healthPercent = percentOk;
    }

    public double getHealthPercent() {
        return healthPercent;
    }

    public String getHealthId() {
        return healthId;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }


    public String toString(){
        return healthId + ": " + level + " " + message ;
    }
}
