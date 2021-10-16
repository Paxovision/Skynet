package com.paxovision.skynet.config;

import com.paxovision.skynet.config.exception.SkynetConfigException;
import com.paxovision.skynet.config.utils.ResourcesList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class SkynetConfigLoader {
    public static final String SKYNET_ENVIRONMENT = "SKYNET_ENVIRONMENT";

    private static SkynetConfigLoader instance = null;
    private  Config config = null;
    private String testEnvironmentName;

    private SkynetConfigLoader(){
        config = ConfigFactory.empty();
    }

    public static SkynetConfigLoader getInstance(){
        if(instance == null){
            instance = new SkynetConfigLoader();
        }
        return instance;
    }

    /**
     * Loads the config by taking the environment name form the
     * environment variable TEST_ENVIRONMENT
     */
    public Config load() {
        String testEnvironment = getSkynetEnvironment();
        return load(testEnvironment);
    }

    public Config reload(){
        instance = new SkynetConfigLoader();
        return load(testEnvironmentName);
    }

    /**
     * Loads the config for the environment provided as the parameter
     */
    public Config load(String testEnvironment) {
        this.testEnvironmentName = testEnvironment;
        if(config != null){
            if (testEnvironmentName != null && testEnvironmentName.trim().length() > 0) {
                log.debug("About to load skynet-{}.conf", testEnvironment);
                Config applicationEnvConfig = ConfigFactory.parseResources("skynet-" + testEnvironmentName + ".conf");
                config = config.withFallback(applicationEnvConfig);//application-dev.conf
            }

            log.debug("About to load skynet.conf");
            Config applicationConfig = ConfigFactory.parseResources("skynet.conf");
            config = config.withFallback(applicationConfig);//application.conf

            log.debug("Locating reference-skynet-X.conf from resource");
            //references
            List<String> files = ResourcesList.getResources(Pattern.compile(".*?reference-skynet-.*\\.conf$"));
            for (int i = files.size() - 1; i >= 0; i--) {
                log.debug("About to load {}", files.get(i));
                config = config.withFallback(ConfigFactory.parseResources(files.get(i)));
            }

            config = config.withFallback(ConfigFactory.systemEnvironment());
            config = config.withFallback(ConfigFactory.systemProperties());
        }
        return config.resolve();
    }

    public Config getConfig() {
        if(config == null){
            throw new SkynetConfigException("Config is loaded. Call loadConfig()");
        }
        return config;
    }

    private String getSkynetEnvironment() {
        String env = System.getenv(SKYNET_ENVIRONMENT);
        log.debug("TEST_ENVIRONMENT found in environment variable: {}", env);
        if (StringUtils.isEmpty(env)) {
            log.debug("TEST_ENVIRONMENT is null, try reading from system property");
            env = System.getProperty(SKYNET_ENVIRONMENT);
            log.debug("TEST_ENVIRONMENT found in system property: {}", env);
        }
        return env;
    }

}