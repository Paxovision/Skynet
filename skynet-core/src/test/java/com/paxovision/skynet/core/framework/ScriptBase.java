package com.paxovision.skynet.core.framework;

import com.paxovision.skynet.core.framework.guice.SkynetCoreTestModule;
import com.paxovision.skynet.core.junit.RequiresInjection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@RequiresInjection(values = {SkynetCoreTestModule.class})
public class ScriptBase {

    static {
        System.setProperty("SKYNET_ENVIRONMENT","unit");
    }

    @BeforeEach
    public void beforeEach(){

    }

    @AfterEach
    public void afterEach(){

    }
}
