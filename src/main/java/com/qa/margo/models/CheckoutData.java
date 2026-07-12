package com.qa.margo.models;

import lombok.Builder;

@Builder(toBuilder = true)
public record CheckoutData (
        String firstName,
        String lastName,
        String zipCode
) { }
