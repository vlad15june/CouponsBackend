package org.coupons.security.cors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by stevehu on 2017-01-21.
 */
public class CorsConfig {
    boolean enabled;
    List allowedOrigins;
    List allowedMethods;

    @JsonIgnore
    String description;

    public CorsConfig() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
