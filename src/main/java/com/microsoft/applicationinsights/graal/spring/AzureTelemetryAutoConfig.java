package com.microsoft.applicationinsights.graal.spring;

import io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(OpenTelemetryAutoConfiguration.class)
@Import({AzureTelemetryConfig.class, OtelGlobalRegistrationConfig.class, JvmMetricsConfig.class})
public class AzureTelemetryAutoConfig {

}
