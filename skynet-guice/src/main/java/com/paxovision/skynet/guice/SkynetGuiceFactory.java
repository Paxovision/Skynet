package com.paxovision.skynet.guice;

import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.google.inject.util.Modules;
import com.paxovision.skynet.guice.exception.SkynetGuiceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

@Slf4j
public class SkynetGuiceFactory {
    private static Injector parentInjector;

    /**
     *Get Injector with default modules.
     */
    public static Injector getInjector() {

        if (parentInjector == null) {
            parentInjector = Guice.createInjector(Modules.combine(new AbstractModule() {
                @Override
                protected void configure() {
                    super.configure();
                    log.debug("Bind Listener Slf4jTypeListener");
                    bindListener(Matchers.any(), new Slf4jTypeListener());
                }
            }));
        }
        return parentInjector;
    }

    /**
     * Get a child injector with additional modules.
     */
    public static Injector getInjector(Module... modules) {
        if (getInjector() != null) {
            log.debug("Creating Child Guice injector... ");
            for(Module m : modules){
                log.debug("Injecting module {}", m.getClass().getName());
            }
            parentInjector =  parentInjector.createChildInjector(modules);
        } else {
            throw new SkynetGuiceException("Parent Guice injector not available");
        }
        return parentInjector;
    }

    public static Injector getInjector(Iterable<? extends Module> modules) {
        if (getInjector() != null) {
            log.debug("Creating Child Guice injector... ");
            for(Module m : modules){
                log.debug("Injecting module {}", m.getClass().getName());
            }
            parentInjector =  parentInjector.createChildInjector(modules);
        } else {
            throw new SkynetGuiceException("Parent Guice injector not available");
        }
        return parentInjector;
    }

    /**
     * Get an instance from guice of give class.
     */
    public static <T> T getInstance(Class<T> clazz) {
        try {
            return getInjector().getInstance(clazz);
        } catch (Throwable ex) {
            throw new SkynetGuiceException(String.format("Unable to load instance for %s!", clazz.getName()), ex);
        }
    }


    public static class Slf4jTypeListener implements TypeListener {

        @Override
        public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
            Class<?> clazz = typeLiteral.getRawType();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getType() == Logger.class &&
                            field.isAnnotationPresent(InjectLogger.class)) {
                        typeEncounter.register(new Slf4jMembersInjector<T>(field));
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    public static class Slf4jMembersInjector<T> implements MembersInjector<T> {
        private final Field field;
        private final Logger logger;

        Slf4jMembersInjector(Field field) {
            this.field = field;
            this.logger = LoggerFactory.getLogger(field.getDeclaringClass());
            field.setAccessible(true);
        }

        @Override
        public void injectMembers(T t) {
            try {
                field.set(t, logger);
            } catch (IllegalAccessException e) {
                throw new SkynetGuiceException(e);
            }
        }
    }
}
