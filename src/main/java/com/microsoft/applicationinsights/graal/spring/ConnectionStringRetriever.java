package com.microsoft.applicationinsights.graal.spring;

import com.azure.monitor.opentelemetry.exporter.implementation.utils.Strings;

import java.util.Optional;

class ConnectionStringRetriever {
    private static final String APPLICATIONINSIGHTS_CONNECTION_STRING_ENV =
            "APPLICATIONINSIGHTS_CONNECTION_STRING";

    private ConnectionStringRetriever() {
    }

    static Optional<String> retrieveConnectionString(String connectionStringSysProp) {

        if(connectionStringSysProp != null) {
            if(connectionStringSysProp.isEmpty()) {
                return Optional.empty();
            }
            String connectionStringWithoutDoubleQuotes = connectionStringSysProp.replaceAll("\"", "");
            return Optional.of(connectionStringWithoutDoubleQuotes);
        }

        String connectionStringFromEnvVar = getEnvVar(APPLICATIONINSIGHTS_CONNECTION_STRING_ENV);
        if (connectionStringFromEnvVar != null) {
            return Optional.of(connectionStringFromEnvVar);
        }

        return Optional.empty();
    }

    private static String getEnvVar(String name) {
        return Strings.trimAndEmptyToNull(System.getenv(name));
    }

}
