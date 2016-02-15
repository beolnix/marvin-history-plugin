package com.beolnix.marvin.im.plugin;

import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * Created by beolnix on 11/9/2015.
 */
public class Activator implements BundleActivator {

    private HistoryIMPlugin historyIMPlugin = new HistoryIMPlugin();

    @Override
    public void start(BundleContext bundleContext) {
        bundleContext.registerService(IMPlugin.class.getName(), historyIMPlugin, null);
    }

    @Override
    public void stop(BundleContext context) {
        historyIMPlugin.shutdown();
    }
}
