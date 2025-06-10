package com.desmond.gadgetstore.common.database;

import lombok.Getter;

@Getter
public enum Tables {
    USERS("users"),
    ORDERS("orders"),
    ORDER_ITEMS("order-items");

    private final String table;

    Tables(String table) {
        this.table = table;
    }
}
