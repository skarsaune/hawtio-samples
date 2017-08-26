package no.kantega.example.application.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * I provide high level insight to the application for management purposes
 */
@Component
@ManagedResource(value="no.kantega.example.application:type=ExampleApplication")
public class ExampleApplicationManagement {

    @ManagedAttribute
    public String getVersion() {
        return versionFromMavenIfPossible("no.kantega.example", "example-application");
    }
    
      
    @ManagedOperation
    public void flushCaches() {
        
    }

    private String versionFromMavenIfPossible(String group, String artifact) {
        final Properties mavenProperties = new Properties();
        String resource = String.format("/META-INF/maven/%s/%s/pom.properties", group, artifact);
        final URL mavenVersionResource = this.getClass().getResource(resource);
        if(mavenVersionResource != null) {
            try {
                mavenProperties.load(mavenVersionResource.openStream());
                return mavenProperties.getProperty("version", "missing version info in " + mavenVersionResource);
            } catch (IOException ignore) {
            }
        }
        return "(DEV VERSION)";
    }{
        
    }
}
