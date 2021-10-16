package com.paxovision.skynet.core.framework.config;

import com.paxovision.skynet.config.SkynetConfig;
import lombok.Getter;

@Getter
public class SkynetCoreTestConfig {

    @SkynetConfig("skynet.core.str-value")
    private String StringValue;
}
