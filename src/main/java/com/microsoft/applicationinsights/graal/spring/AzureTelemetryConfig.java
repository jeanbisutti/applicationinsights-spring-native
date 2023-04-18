package com.microsoft.applicationinsights.graal.spring;

import com.azure.monitor.opentelemetry.exporter.AzureMonitorExporterBuilder;
import io.opentelemetry.api.logs.GlobalLoggerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.logs.export.SimpleLogRecordProcessor;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class AzureTelemetryConfig {

    private static final Logger LOGGER = Logger.getLogger(AzureTelemetryConfig.class.getName());

    private static final String CONNECTION_STRING_ERROR_MESSAGE = "Unable to find the Application Insights connection string.";

    private AzureMonitorExporterBuilder azureMonitorExporterBuilder;

    public AzureTelemetryConfig(@Value("${applicationinsights.connection.string:}") String connectionStringSysProp) {
        if (AzureTelemetry.isEnabled()) {
            this.azureMonitorExporterBuilder = createAzureMonitorExporterBuilder(connectionStringSysProp);
        } else {
            LOGGER.log(Level.INFO, "Application Insights for Spring native is disabled for a non-native image runtime environment. We recommend using the Application Insights Java agent.");
        }
    }

    private AzureMonitorExporterBuilder createAzureMonitorExporterBuilder(String connectionStringSysProp) {
        Optional<String> connectionString = ConnectionStringRetriever.retrieveConnectionString(connectionStringSysProp);
        if (connectionString.isPresent()) {
            try {
                return new AzureMonitorExporterBuilder().connectionString(connectionString.get());
            } catch (IllegalArgumentException illegalArgumentException) {
                String errorMessage = illegalArgumentException.getMessage();
                if (errorMessage.contains("InstrumentationKey")) {
                    LOGGER.log(Level.WARNING, CONNECTION_STRING_ERROR_MESSAGE + " Please check you have not used an instrumentation key instead of a connection string");
                }
            }
        } else {
            LOGGER.log(Level.WARNING, CONNECTION_STRING_ERROR_MESSAGE);
        }
        return null;
    }


    @Bean
    public MetricExporter metricExporter() {
        if (azureMonitorExporterBuilder == null) {
            return null;
        }
        return azureMonitorExporterBuilder.buildMetricExporter();
    }

    @Bean
    public SpanExporter spanExporter() {
        if (azureMonitorExporterBuilder == null) {
            return null;
        }
        return azureMonitorExporterBuilder.buildTraceExporter();
    }

    @Bean
    public LogRecordExporter logRecordExporter() {
        if (azureMonitorExporterBuilder == null) {
            return null;
        }
        LogRecordExporter logRecordExporter = azureMonitorExporterBuilder.buildLogRecordExporter();
        initOTelLogger(logRecordExporter);
        return logRecordExporter;
    }

    public void initOTelLogger(LogRecordExporter logRecordExporter) {
        if (azureMonitorExporterBuilder != null) {
            SdkLoggerProvider loggerProvider =
                    SdkLoggerProvider.builder()
                            .addLogRecordProcessor(SimpleLogRecordProcessor.create(logRecordExporter))
                            .build();
            GlobalLoggerProvider.set(loggerProvider);
        }
    }

}
