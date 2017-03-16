/**
 * Copyright (C) 2004-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gooddata.GoodData;
import com.gooddata.GoodDataRestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ResponseErrorHandlerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ResponseErrorHandler responseErrorHandler;

    @BeforeMethod
    public void setUp() throws Exception {
        responseErrorHandler = new ResponseErrorHandler(singletonList(new MappingJackson2HttpMessageConverter(OBJECT_MAPPER)));
    }

    @Test
    public void testHandleGdcError() throws Exception {
        final ClientHttpResponse response = prepareResponse("/gdc/gdcError.json");

        final GoodDataRestException exc = assertException(() -> responseErrorHandler.handleError(response));

        assertThat("GoodDataRestException should have been thrown!", exc, is(notNullValue()));
        assertEquals(exc.getStatusCode(), 500);
        assertEquals(exc.getRequestId(), "REQ");
        assertEquals(exc.getComponent(), "COMPONENT");
        assertEquals(exc.getErrorClass(), "CLASS");
        assertEquals(exc.getErrorCode(), "CODE");
        assertEquals(exc.getText(), "MSG");
    }

    @Test
    public void testHandleErrorStructure() throws Exception {
        final ClientHttpResponse response = prepareResponse("/gdc/errorStructure.json");

        final GoodDataRestException exc = assertException(() -> responseErrorHandler.handleError(response));

        assertThat("GoodDataRestException should have been thrown!", exc, is(notNullValue()));
        assertEquals(exc.getStatusCode(), 500);
        assertEquals(exc.getRequestId(), "REQ");
        assertEquals(exc.getComponent(), "COMPONENT");
        assertEquals(exc.getErrorClass(), "CLASS");
        assertEquals(exc.getErrorCode(), "CODE");
        assertEquals(exc.getText(), "MSG PARAM1 PARAM2 3");
    }

    private ClientHttpResponse prepareResponse(String resourcePath) throws IOException {
        final ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(response.getRawStatusCode()).thenReturn(500);
        final HttpHeaders headers = new HttpHeaders();
        headers.set(GoodData.GDC_REQUEST_ID_HEADER, "requestId");
        headers.setContentType(MediaType.APPLICATION_JSON);
        when(response.getHeaders()).thenReturn(headers);
        when(response.getBody()).thenReturn(getClass().getResourceAsStream(resourcePath));

        return response;
    }

    private static GoodDataRestException assertException(Runnable runnable) {
        try {
            runnable.run();
            return null;
        } catch (GoodDataRestException e) {
            return e;
        }
    }

}