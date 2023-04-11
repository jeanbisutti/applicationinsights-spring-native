# Application Insights for Spring native

This project allows providing telemetry data on Microsoft Azure for [Spring Boot applications packaged as GraalVM native images](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html).

## How to use this project

### Enable telemetry

First, you have to install this project in your local repository (`mvn install`) or add the following repository to your project:

```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

After, you have to add the following dependency:

```xml
    <dependency>
        <groupId>com.github.jeanbisutti</groupId>
        <artifactId>applicationinsights-spring-native</artifactId>
        <version>main-SNAPSHOT</version>
    </dependency>
```

This dependency is a Spring Boot starter that will provide telemetry data for HTTP requests.

You can configure additional [OpenTelemetry instrumentations](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/docs/supported-libraries.md#libraries--frameworks).

Project examples using Application Insights for Spring native:
* [Spring Petclinic application](https://github.com/jeanbisutti/spring-petclinic/blob/native-ai/readme.md)
* [A basic Spring Boot application](https://github.com/jeanbisutti/spring-native-image-aot-inst-poc/tree/master/otel-programmatic-instrumentation)

_If you have an issue with OpenTelemetry instrumentation libraries used with native images, don't hesitate to raise an issue on OpenTelemetry side or this project._

### Configure a connection string to an Application Insights resource on Azure

First, [copy the connection string of your Application Insights resoure](https://learn.microsoft.com/en-us/azure/azure-monitor/app/java-standalone-config#connection-string).

You can then configure the connection string in two different ways:
* With the `APPLICATIONINSIGHTS_CONNECTION_STRING` environment variable 
* With the `applicationinsights.connection.string` system property. You can use `-Dapplicationinsights.connection.string` or add the property to your `application.properties` file.

After, you can build your application as a native image and start the native image.

An example:

```
mvn -Pnative spring-boot:build-image
docker run -e APPLICATIONINSIGHTS_CONNECTION_STRING="{CONNECTION_STRING}" {image-name} 
```
where you have to replace `{CONNECTION_STRING}` and `{image-name}` by your connection string and the image name.
