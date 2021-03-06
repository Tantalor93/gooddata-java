/*
 * Copyright (C) 2007-2016, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata.notification;

import static com.gooddata.util.Validate.notEmpty;
import static com.gooddata.util.Validate.notNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.springframework.web.util.UriTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Project event.
 *
 * Serialization only.
 */
@JsonTypeName("projectEvent")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ProjectEvent {

    public static final String URI = "/gdc/projects/{projectId}/notifications/events";
    public static final UriTemplate TEMPLATE = new UriTemplate(URI);

    private final String type;
    private final Map<String, String> parameters;

    public ProjectEvent(final String type) {
        this(type, new HashMap<String, String>());
    }

    public ProjectEvent(final String type, final Map<String, String> parameters) {
        notEmpty(type, "type can't be empty");
        notNull(parameters, "parameters can't be null");
        this.type = type;
        this.parameters = parameters;
    }

    /**
     * Set the parameter with given key and value, overwriting the preceding value of the same key (if any).
     * @param key parameter key
     * @param value parameter value
     */
    public void setParameter(final String key, final String value) {
        notNull(key, "parameter key can't be null");
        notNull(value, "parameter value can't be null");
        parameters.put(key, value);
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("parameters")
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProjectEvent))
            return false;

        ProjectEvent that = (ProjectEvent) o;

        if (type != null ? !type.equals(that.type) : that.type != null)
            return false;
        return !(parameters != null ? !parameters.equals(that.parameters) : that.parameters != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
