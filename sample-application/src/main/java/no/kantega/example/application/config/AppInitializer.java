package no.kantega.example.application.config;

import static java.util.EnumSet.allOf;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitializer implements WebApplicationInitializer {

    private static final String CONFIG_LOCATION = "no.kantega.example.application.config";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));

        servletContext.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        servletContext.setInitParameter("contextConfigLocation", SpringConfig.class.getName());

        
        servletContext.addFilter("requestContextFilter", RequestContextFilter.class).addMappingForUrlPatterns(allOf(DispatcherType.class), false, "/*");
        servletContext.addServlet("dispatcherServlet", new DispatcherServlet(context)).addMapping("/*");
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        return context;
    }
}
