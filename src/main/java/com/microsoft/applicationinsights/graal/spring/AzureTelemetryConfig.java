package com.microsoft.applicationinsights.graal.spring;

import com.azure.monitor.opentelemetry.exporter.AzureMonitorExporterBuilder;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.logs.GlobalLoggerProvider;
import io.opentelemetry.instrumentation.runtimemetrics.*;
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

    private static final Logger LOGGER = Logger.getLogger(ConnectionStringRetriever.class.getName());
    private AzureMonitorExporterBuilder azureMonitorExporterBuilder;

    public AzureTelemetryConfig(@Value("${applicationinsights.connection.string:}") String connectionStringSysProp) {
        Optional<String> connectionString = ConnectionStringRetriever.retrieveConnectionString(connectionStringSysProp);
        if (connectionString.isPresent()) {
            azureMonitorExporterBuilder = new AzureMonitorExporterBuilder().connectionString(connectionString.get());
        } else {
            LOGGER.log(Level.WARNING, "Unable to find the Application Insights connection string.");
        }
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
        return azureMonitorExporterBuilder.buildLogRecordExporter();
    }

    @Bean
    public Void initOTelLogger(LogRecordExporter logRecordExporter) {
        if (azureMonitorExporterBuilder != null) {
            SdkLoggerProvider loggerProvider =
                    SdkLoggerProvider.builder()
                            .addLogRecordProcessor(SimpleLogRecordProcessor.create(logRecordExporter))
                            .build();
            GlobalLoggerProvider.set(loggerProvider);

        }
        return null;
    }

    @Bean
    public Void initJvmMetrics(OpenTelemetry opentelemetry) {
        if (azureMonitorExporterBuilder != null) {
            BufferPools.registerObservers(opentelemetry);
            Classes.registerObservers(opentelemetry);
            Cpu.registerObservers(opentelemetry);
            MemoryPools.registerObservers(opentelemetry);
            Threads.registerObservers(opentelemetry);
            GarbageCollector.registerObservers(opentelemetry);
        }
        return null;
    }

}
