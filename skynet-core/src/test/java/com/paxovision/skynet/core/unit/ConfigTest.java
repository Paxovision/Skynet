package com.paxovision.skynet.core.unit;

import com.google.inject.Inject;
import com.paxovision.skynet.core.framework.ScriptBase;
import com.paxovision.skynet.core.framework.config.SkynetCoreTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigTest extends ScriptBase {

    @Inject
    SkynetCoreTestConfig skynetCoreTestConfig;

    @Test
    public void test0(){
        Assertions.assertThat(skynetCoreTestConfig).isNotNull();
    }

    @Test
    public void test1(){
        Assertions.assertThat(skynetCoreTestConfig.getStringValue()).isEqualTo("This is string value from unit test");
    }
}
