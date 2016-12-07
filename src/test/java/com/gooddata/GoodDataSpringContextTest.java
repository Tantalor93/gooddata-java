package com.gooddata;

import com.gooddata.project.Project;
import com.gooddata.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.testng.Assert.*;

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class GoodDataSpringContextTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ApplicationContext appContext;


    @Test
    public void allServicesRegisteredAsBeans() throws Exception {
        Arrays.stream(GoodData.class.getDeclaredMethods())
                .map(Method::getReturnType)
                .filter(rt -> rt.getName().endsWith("Service"))
                .forEach(rt -> assertNotNull(appContext.getBean(rt), "Bean of type " + rt + " should be registered"));

    }
}