package com.microsoft.applicationinsights.graal.spring;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.runtimemetrics.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

class JvmMetricsPostProcessor implements BeanPostProcessor, Ordered {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof OpenTelemetry openTelemetry) {
            BufferPools.registerObservers(openTelemetry);
            Classes.registerObservers(openTelemetry);
            Cpu.registerObservers(openTelemetry);
            MemoryPools.registerObservers(openTelemetry);
            Threads.registerObservers(openTelemetry);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
