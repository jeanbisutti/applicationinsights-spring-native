package com.microsoft.applicationinsights.graal.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelGlobalRegistrationConfig {

    @Bean
    public OtelGlobalRegistrationPostProcessor otelGlobalRegistrationPostProcessor() {
        return new OtelGlobalRegistrationPostProcessor();
    }

}
