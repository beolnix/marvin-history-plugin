package com.beolnix.marvin.im.plugin;

import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Created by beolnix on 11/9/2015.
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) {
        bundleContext.registerService(IMPlugin.class.getName(), new HistoryIMPlugin(), null);
    }

    @Override
    public void stop(BundleContext context) {
        // NOTE: The service is automatically unregistered.
    }
}
