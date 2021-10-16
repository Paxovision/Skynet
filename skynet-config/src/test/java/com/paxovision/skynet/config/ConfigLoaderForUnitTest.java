package com.paxovision.skynet.config;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class ConfigLoaderForUnitTest {
    SkynetConfigLoader skynetConfigLoader;
    Config config;

    static {
        System.setProperty("SKYNET_ENVIRONMENT","unit");
    }

    @BeforeEach
    public void beforeEach(){
        log.info("beforeEach");
        skynetConfigLoader = SkynetConfigLoader.getInstance();
        config = skynetConfigLoader.load();
    }

    @Test
    public void test1(){
        log.info("start test name test1");
        config = skynetConfigLoader.load();
       Assertions.assertThat(config.getString("skynet.config.version")).isEqualTo("1.0 from skynet-unit file");
    }
}
