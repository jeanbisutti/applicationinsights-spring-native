# Application Insights for Spring native

This project allows providing telemetry data on Microsoft Azure for [Spring Boot applications packaged as GraalVM native images](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html).

## How to use this project

### Instrument the code

You have to use the following dependency:
```xml
<dependency>
    <groupId>org.jeanbisutti</groupId>
    <artifactId>applicationinsights-spring-native</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```
This dependency is a Spring Boot starter that will provide telemetry data for HTTP requests and [some JVM metrics](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/runtime-metrics/library#jvm-runtime-metrics).

You can configure additional [OpenTelemetry instrumentations](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/docs/supported-libraries.md#libraries--frameworks).

With this project, the [OpenTelemetry instrumentation for JDBC](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/jdbc/library) and the [OpenTelemetry Logback appender](https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/logback/logback-appender-1.0/library) should work. A project example is available [here](https://github.com/jeanbisutti/spring-native-image-aot-inst-poc/tree/master/otel-programmatic-instrumentation).

If you have an issue with other OpenTelemetry instrumentation libraries  used with native images, don't hesitate to raise an issue on OpenTelemetry side or this project.

### Configure a connection string to an Application Insights resource on Azure

You can do it in two different ways:
* With the `APPLICATIONINSIGHTS_CONNECTION_STRING` environment variable 
* With the `applicationinsights.connection.string` system property. You can use `-Dapplicationinsights.connection.string` or add the property to your `application.properties` file.

After, you can build your application as a native image and start the native image.

An example:

```
mvn -Pnative spring-boot:build-image
docker run -e APPLICATIONINSIGHTS_CONNECTION_STRING="{CONNECTION_STRING}" {image-name} 
```
where you have to replace `{CONNECTION_STRING}` and `{image-name}` by your connection string and the image name.
