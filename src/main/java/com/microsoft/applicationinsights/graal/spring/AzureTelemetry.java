package com.microsoft.applicationinsights.graal.spring;

public class AzureTelemetry {

    private static final String APPLICATIONINSIGHTS_NON_NATIVE_ENABLED = "applicationinsights.native.spring.non-native.enabled";

    private AzureTelemetry() {
    }

    public static boolean isEnabled() {
        return isNativeRuntimeExecution() || Boolean.getBoolean(APPLICATIONINSIGHTS_NON_NATIVE_ENABLED);
    }

    private static boolean isNativeRuntimeExecution() {
        String imageCode = System.getProperty("org.graalvm.nativeimage.imagecode");
        return imageCode != null;
    }

}
