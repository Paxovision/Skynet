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
public class ConfigLoaderTest {
    SkynetConfigLoader skynetConfigLoader;
    Config config;

    static {
        System.setProperty("SKYNET_ENVIRONMENT","");
        SkynetConfigLoader.getInstance().reload();
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
        Assertions.assertThat(config.getString("skynet.config.version")).isEqualTo("1.0 from skynet file");
    }

    @Test
    public void test2(){
        log.info("start test name test2");
        Assertions.assertThat(config.getString("user.dir")).endsWith("Skynet/skynet-config");
    }

    @Test
    public void test3(){
        log.info("start test name test3");
        Assertions.assertThat(config.getString("skynet.config.name")).isEqualTo("skynet-config");
    }

}
