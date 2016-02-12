package com.beolnix.marvin.im.plugin;

import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


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
