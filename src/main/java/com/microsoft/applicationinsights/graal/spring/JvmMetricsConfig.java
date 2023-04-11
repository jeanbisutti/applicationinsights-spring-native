package com.microsoft.applicationinsights.graal.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class JvmMetricsConfig {

   // @Bean
    public JvmMetricsPostProcessor jvmMetricsPostProcessor() {
        return new JvmMetricsPostProcessor();
    }

}
