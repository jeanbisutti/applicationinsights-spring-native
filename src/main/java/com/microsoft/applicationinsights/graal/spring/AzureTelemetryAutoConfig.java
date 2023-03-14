package com.microsoft.applicationinsights.graal.spring;

import io.opentelemetry.instrumentation.spring.autoconfigure.EnableOpenTelemetry;
import io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(OpenTelemetryAutoConfiguration.class)
@Import({AzureTelemetryConfig.class, OtelGlobalRegistrationConfig.class})
@EnableOpenTelemetry // Could be removed after the otel-java-instrumentation release in march
public class AzureTelemetryAutoConfig {

}
