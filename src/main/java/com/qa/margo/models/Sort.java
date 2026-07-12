package com.qa.margo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sort {
    NAME_ASC("az"),
    NAME_DESC("za") ,
    PRICE_LOW("lohi"),
    PRICE_HIGH("hilo");

    private final String value;
}
