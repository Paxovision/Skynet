package com.paxovision.skynet.core.framework.guice;

import com.google.inject.AbstractModule;
import com.paxovision.skynet.core.guice.SkynetCoreModule;
import lombok.extern.slf4j.Slf4j;
import com.paxovision.skynet.core.framework.config.SkynetCoreTestConfig;

@Slf4j
public class SkynetCoreTestModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        install(new SkynetCoreModule());

        bind(SkynetCoreTestConfig.class);
    }
}
