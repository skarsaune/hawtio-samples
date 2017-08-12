package no.kantega.example.application.hawtio.plugin;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.hawt.web.plugin.HawtioPlugin;

public class ExamplePluginNotifier implements ServletContextListener {

  private static final Logger LOG = LoggerFactory.getLogger(ExamplePluginNotifier.class);

  HawtioPlugin plugin = null;


  public void contextInitialized(ServletContextEvent servletContextEvent) {

    plugin = new HawtioPlugin();
    plugin.setContext("example-plugin");
    plugin.setName("plugin_example");
    plugin.setScripts("plugin/js/exampePlugin.js");
    plugin.setDomain("no.kantega.example.application");
    plugin.init();
    
    LOG.info("Initialized {} plugin", plugin.getName());
    

  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    plugin.destroy();
    LOG.info("Destroyed {} plugin", plugin.getName());
  }
}
