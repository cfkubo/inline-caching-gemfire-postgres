package com.example.config;

import com.example.model.DataItem;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GemFireConfig {

    @Bean
    public ClientCache clientCache() {
        return new ClientCacheFactory()
                .setPoolSubscriptionEnabled(true)
                .create();
    }

    @Bean
    public Region<String, DataItem> dataItemRegion(ClientCache clientCache) {
        ClientRegionFactory<String, DataItem> regionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
        return regionFactory.create("dataItemRegion");
    }
}