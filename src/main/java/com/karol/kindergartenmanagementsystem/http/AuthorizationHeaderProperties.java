package com.karol.kindergartenmanagementsystem.http;

import lombok.Getter;

@Getter
public enum AuthorizationHeaderProperties {
    AUTHORIZATION_HEADER("Authorization"),
    TOKEN_PREFIX("Bearer ");

    final String value;

    AuthorizationHeaderProperties(String value) {
        this.value = value;
    }
}
