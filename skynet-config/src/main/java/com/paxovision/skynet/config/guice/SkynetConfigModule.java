package com.paxovision.skynet.config.guice;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.paxovision.skynet.config.SkynetConfig;
import com.paxovision.skynet.config.exception.SkynetConfigException;
import com.paxovision.skynet.config.SkynetConfigLoader;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class SkynetConfigModule extends AbstractModule {
    //private static Config config;

    @Override
    protected void configure() {
        super.configure();
        log.debug("Loading Skynet config ...");
        SkynetConfigLoader.getInstance().load();

        log.debug("Bind Listener SkynetConfigListener");
        bindListener(Matchers.any(), new SkynetConfigListener());
    }

    /**
     * SkynetConfigListener listener listens to the SkynetConfig in any class the Guice is asked to bind.
     * If a field of type String which have an annotation @SkynetConfig is found, the SkynetTypesafeConfigMembersInjector
     * is called where the value is fetched from the config files and updated in the instance
     */
    public static class SkynetConfigListener implements TypeListener {
        @Override
        public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
            Class<?> clazz = typeLiteral.getRawType();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(SkynetConfig.class)) {
                        typeEncounter.register(new SkynetTypesafeConfigMembersInjector<>(field, field.getAnnotation(SkynetConfig.class).value()));
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    /**
     * SkynetTypesafeConfigMembersInjector injector is invoked by the listener when a field which has the annotation @SkynetConfig
     * is found in the classes that Guice is asked to bind. SkynetTypesafeConfigMembersInjector fetches the value form the
     * config files and then bind them to the classes.
     */
    public static class SkynetTypesafeConfigMembersInjector<T> implements MembersInjector<T> {
        private final Field field;
        private T fieldValue;
        Config config = SkynetConfigLoader.getInstance().getConfig();

        SkynetTypesafeConfigMembersInjector(Field field, String key) {
            this.field = field;
            if (config != null) {
                ConfigValue value = config.getValue(key);
                if (field.getType() == String.class) {
                    this.fieldValue = (T) value.unwrapped().toString();
                } else if (field.getType() == Integer.TYPE || field.getType() == Boolean.TYPE) {
                    this.fieldValue = (T) value.unwrapped();
                } else if (field.getType() == Config.class) {
                    this.fieldValue = (T) config.getConfig(key);
                }
            }
            field.setAccessible(true);
        }

        @Override
        public void injectMembers(T t) {
            try {
                if (fieldValue != null) {
                    field.set(t, fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new SkynetConfigException(e);
            }
        }
    }
}
