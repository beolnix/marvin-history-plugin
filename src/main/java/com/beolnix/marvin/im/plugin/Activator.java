package com.beolnix.marvin.im.plugin;

import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by beolnix on 11/9/2015.
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) {
        ApplicationContext ctx = setupSpringAppContext(bundleContext);
        IMPlugin plugin = ctx.getBean(HistoryIMPlugin.class);
        bundleContext.registerService(IMPlugin.class.getName(), plugin, null);
    }

    public ApplicationContext setupSpringAppContext(BundleContext bundleContext) {
        Bundle bundle = bundleContext.getBundle();
//        bundle.adapt(BundleWiring.class).listResources("/", "*.class", BundleWiring.LISTRESOURCES_LOCAL | BundleWiring.LISTRESOURCES_RECURSE);
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(HistoryConfiguration.class);
        ((ConfigurableBeanFactory)ctx.getParentBeanFactory()).setBeanClassLoader(this.getClass().getClassLoader());
        ctx.refresh();
        return ctx;
    }


        @Override
    public void stop(BundleContext context) {
        // NOTE: The service is automatically unregistered.
    }
}
