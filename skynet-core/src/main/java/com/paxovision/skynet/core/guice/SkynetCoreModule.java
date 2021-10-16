package com.paxovision.skynet.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.paxovision.skynet.config.guice.SkynetConfigModule;
import com.paxovision.skynet.core.data.SkynetDataContext;
import com.paxovision.skynet.core.log.LogReport;
import com.paxovision.skynet.core.log.LogReportEnable;
import com.paxovision.skynet.core.log.SkynetLogger;
import lombok.extern.slf4j.Slf4j;
import net.jmob.guice.conf.core.ConfigurationModule;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Slf4j
public class SkynetCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        install(new SkynetConfigModule());
        install(new ConfigurationModule());

        log.debug("Bind Interceptor LogReportMethodInterceptor");
        bindInterceptor(
                Matchers.annotatedWith(LogReportEnable.class),
                Matchers.annotatedWith(LogReport.class),
                new LogReportMethodInterceptor()
        );

        bind(SkynetDataContext.class).toProvider(SkynetDataContextProvider.class).in(Singleton.class);

    }



    public static class LogReportMethodInterceptor implements MethodInterceptor {

        private Logger log = LoggerFactory.getLogger(SkynetLogger.class);

        public LogReportMethodInterceptor(){
            log.debug("Creating instance of LogReportMethodInterceptor");
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Method method = methodInvocation.getMethod();
            if (Modifier.toString(method.getModifiers()).contains("public") ){
                if (method.isAnnotationPresent(LogReport.class)) {
                    LogReport logReport = method.getAnnotation(LogReport.class);
                    String name = logReport.name();
                    if (name == null || name.length() == 0) {
                        name = method.getName();
                    }
                    String description = logReport.description();
                    if (description == null || description.length() == 0) {
                        description = "";
                    }

                    Object[] args = methodInvocation.getArguments();
                    if (args != null && args.length > 0) {
                        log.info(name + ": " + description, args);
                    } else {
                        log.info(name + ": " + description);
                    }
                }
            }
            return methodInvocation.proceed();
        }
    }
}
