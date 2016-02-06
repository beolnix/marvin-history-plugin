package com.beolnix.marvin.im.plugin.history.configuration;


import com.beolnix.marvin.im.plugin.history.HistoryIMPlugin;
import com.beolnix.marvin.plugins.api.IMPlugin;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistoryPluginConfiguration {

    @Autowired
    public IMPlugin getPlugin(BundleContext bundleContext) {
        return new HistoryIMPlugin(bundleContext);
    }

}
