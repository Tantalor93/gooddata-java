/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.md.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Collection;

/**
 * Report definition content
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "format")
@JsonSubTypes({
        @JsonSubTypes.Type(name = GridReportDefinitionContent.FORMAT, value = GridReportDefinitionContent.class),
        @JsonSubTypes.Type(name = OneNumberReportDefinitionContent.FORMAT,
                value = OneNumberReportDefinitionContent.class)
        //TODO chart
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ReportDefinitionContent {

    private final String format;
    private final Grid grid;
    private final Collection<Filter> filters;

    public ReportDefinitionContent(String format, Grid grid, Collection<Filter> filters) {
        this.format = format;
        this.grid = grid;
        this.filters = filters;
    }

    @JsonIgnore // handled by type info
    public String getFormat() {
        return format;
    }

    public Grid getGrid() {
        return grid;
    }

    public Collection<Filter> getFilters() {
        return filters;
    }
}
