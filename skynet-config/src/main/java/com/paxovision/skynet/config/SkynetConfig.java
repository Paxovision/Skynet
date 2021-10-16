package com.paxovision.skynet.config;

import com.typesafe.config.Config;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SkynetConfig annotation is used annotate fields.
 * This annotaion denotes that a value from the config files will be fetched for the value provided
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SkynetConfig {

    /**
     * @return the {@link Config} path to the configuration value.
     */
    String value();

}
