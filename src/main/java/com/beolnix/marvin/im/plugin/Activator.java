package com.beolnix.marvin.im.plugin;

import com.beolnix.marvin.plugins.api.IMPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by beolnix on 11/9/2015.
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) {
        ApplicationContext ctx = setupSpringAppContext(bundleContext);
        IMPlugin plugin = ctx.getBean(IMPlugin.class);

        bundleContext.registerService(IMPlugin.class.getName(), plugin, null);
    }

    public ApplicationContext setupSpringAppContext(BundleContext bundleContext) {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        ApplicationContext parentAppCtx = new AnnotationConfigApplicationContext();
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) parentAppCtx).getBeanFactory();
        beanFactory.registerSingleton("bundleContext", bundleContext);
        ((ConfigurableApplicationContext) parentAppCtx).refresh();

        String[] configLocations = {"/plugin-context.xml"};
        return new ClassPathXmlApplicationContext(configLocations, parentAppCtx);
    }

    @Override
    public void stop(BundleContext context) {
        // NOTE: The service is automatically unregistered.
    }
}
