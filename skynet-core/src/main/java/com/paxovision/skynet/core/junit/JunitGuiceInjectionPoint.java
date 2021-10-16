package com.paxovision.skynet.core.junit;

import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.paxovision.skynet.guice.SkynetGuiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.google.inject.Module;

@Slf4j
public class JunitGuiceInjectionPoint implements BeforeTestExecutionCallback, BeforeEachCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {

        /*List<Module> modules = Lists.newArrayList(new ConfigurationModule());

        Optional<Object> test = context.getTestInstance();

        if (test.isPresent()) {
            //RequiresInjection requiresInjection = test.get().getClass().getAnnotation(RequiresInjection.class);
            RequiresInjection requiresInjection = getAnnotationFromType(test.get().getClass(),RequiresInjection.class);
            if (requiresInjection != null) {
                for (Class c : requiresInjection.values()) {
                    modules.add((Module) c.newInstance());
                }
            }

            Module aggregate = Modules.combine(modules);
            Injector injector = Guice.createInjector(aggregate);

            injector.injectMembers(test.get());
            getStore(context).put(injector.getClass(), injector);
        }*/

    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass()));
    }

    private <A extends Annotation> A getAnnotationFromType(Class<?> classType, final Class<A> annotationClass) {

        while ( !classType.getName().equals(Object.class.getName()) ) {

            if ( classType.isAnnotationPresent(annotationClass)) {
                return classType.getAnnotation(annotationClass);
            }
            classType = classType.getSuperclass();
        }
        return null;

    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Injector parentInjector = SkynetGuiceFactory.getInjector();

        List<Module> modules = new ArrayList<>();
        //Module module = new ConfigurationModule();
        //modules.add(module);

        Optional<Object> test = context.getTestInstance();
        if (test.isPresent()) {
            RequiresInjection requiresInjection = getAnnotationFromType(test.get().getClass(), RequiresInjection.class);
            if (requiresInjection != null) {
                for (Class c : requiresInjection.values()) {
                    log.debug("Combining Guice module {}", c.getName());
                    modules.add((Module) c.newInstance());
                }
            }

            Module aggregate = Modules.combine(modules);

            Injector injector = SkynetGuiceFactory.getInjector(aggregate);

            injector.injectMembers(test.get());
            getStore(context).put(injector.getClass(), injector);
        }
    }
}
