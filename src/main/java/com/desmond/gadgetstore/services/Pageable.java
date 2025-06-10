package com.desmond.gadgetstore.services;

import org.springdoc.core.converters.models.Sort;

public interface Pageable {
    int getPageNumber();

    int getPageSize();

    long getOffset();

    Sort getSort();

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    boolean hasPrevious();
}
