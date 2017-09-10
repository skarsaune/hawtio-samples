package no.kantega.example.application;

import static java.util.EnumSet.allOf;
import static org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import no.kantega.example.application.config.SpringConfig;

/**
 * I launch the application in an embedded Jetty
 */

public class ExampleApplicationMain {
    private static final Logger log = LoggerFactory.getLogger(ExampleApplicationMain.class);

    private int webappPort;

    private Server server;


    public ExampleApplicationMain(Integer webappPort) {

        this.webappPort = webappPort;
        this.server = new Server(this.webappPort);
    }


    public static void main(String[] args) {
        try {
            Integer webappPort = Integer.parseInt(System.getProperty("server.port", "9898"));
            ExampleApplicationMain application = new ExampleApplicationMain(webappPort);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    log.debug("ShutdownHook triggered. Exiting glnConfigMain");
                    application.stop();
                }
            });
            application.start();
            log.debug("Finished waiting for Thread.currentThread().join()");
            application.stop();
        } catch (RuntimeException|Error e) {
            log.error("Error during startup.", e);
            System.exit(1);
        }
    }

    public void start() {
//        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(new no.kantega.example.application.config.JerseyConfig()));

        ServletContextHandler context = new ServletContextHandler();
//        context.addServlet(jerseyServlet, "/*");

        context.addEventListener(new ContextLoaderListener());
        context.addEventListener(new RequestContextListener());

        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.setInitParameter("contextConfigLocation", SpringConfig.class.getName());

//        context.getServletContext().addServlet("dispatcherServlet", DispatcherServlet.class).addMapping("/*");
        //Spring security
        FilterRegistration.Dynamic securityFilter = context.getServletContext()
                                                           .addFilter(DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        securityFilter.addMappingForUrlPatterns(allOf(DispatcherType.class), false, "/*");
        

        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            log.error("Error during Jetty startup. Exiting", e);
            System.exit(2);
        }
        int localPort = getPort();
        log.info("Server started on http://localhost:{}", localPort);
        try {
            server.join();
        } catch (InterruptedException e) {
            log.error("Jetty server thread when join. Pretend everything is OK.", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.warn("Error when stopping Jetty server", e);
        }
    }

    public int getPort() {
        return webappPort;
    }
}
