package com.joon.sunguard_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my.test")
public class SetYaml {

    private String value;

    // getter, setter
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}