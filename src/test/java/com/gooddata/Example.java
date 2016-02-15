/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata;

import com.gooddata.account.Account;
import com.gooddata.account.AccountService;
import com.gooddata.dataset.DatasetManifest;
import com.gooddata.dataset.DatasetService;
import com.gooddata.gdc.DataStoreService;
import com.gooddata.http.client.GoodDataHttpClient;
import com.gooddata.http.client.LoginSSTRetrievalStrategy;
import com.gooddata.http.client.SSTRetrievalStrategy;
import com.gooddata.http.client.SimpleSSTRetrievalStrategy;
import com.gooddata.md.Attribute;
import com.gooddata.md.Fact;
import com.gooddata.md.MetadataService;
import com.gooddata.md.Metric;
import com.gooddata.md.report.AttributeInGrid;
import com.gooddata.md.report.GridElement;
import com.gooddata.md.report.GridReportDefinitionContent;
import com.gooddata.md.report.Report;
import com.gooddata.md.report.ReportDefinition;
import com.gooddata.model.ModelDiff;
import com.gooddata.model.ModelService;
import com.gooddata.project.Project;
import com.gooddata.project.ProjectService;
import com.gooddata.report.ReportExportFormat;
import com.gooddata.report.ReportService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import static com.gooddata.md.Restriction.identifier;
import static java.util.Arrays.asList;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.pool.ConnPoolControl;

public class Example {

    public static void main(String... args) throws IOException {
      /*  final GoodData gd = new GoodData("roman@gooddata.com", "Roman1");


        final AccountService accountService = gd.getAccountService();
        final Account current = accountService.getCurrent();
        System.out.println(current.getId());


        final ProjectService projectService = gd.getProjectService();

        final Collection<Project> projects = projectService.getProjects();
        System.out.println(projects);

        final Project project = projectService.createProject(new Project("sparkling3", "pgroup2")).get();
        System.out.println(project.getUri());


        final ModelService modelService = gd.getModelService();

        final ModelDiff projectModelDiff = modelService.getProjectModelDiff(project,
                new InputStreamReader(Example.class.getResourceAsStream("/person.json"))).get();
        modelService.updateProjectModel(project, projectModelDiff).get();


        final MetadataService md = gd.getMetadataService();

        String fact = md.getObjUri(project, Fact.class, identifier("fact.person.shoesize"));
        Metric m = md.createObj(project, new Metric("Avg shoe size", "SELECT AVG([" + fact + "])", "#,##0"));

        Attribute attr = md.getObj(project, Attribute.class, identifier("attr.person.department"));

        ReportDefinition definition = GridReportDefinitionContent.create(
                "Department avg shoe size",
                asList("metricGroup"),
                asList(new AttributeInGrid(attr.getDefaultDisplayForm().getUri())),
                asList(new GridElement(m.getUri(), "Avg shoe size"))
        );
        definition = md.createObj(project, definition);
        Report report = md.createObj(project, new Report(definition.getTitle(), definition));


        final DatasetService datasetService = gd.getDatasetService();

        final DatasetManifest manifest = datasetService.getDatasetManifest(project, "dataset.person");
        datasetService.loadDataset(project, manifest, Example.class.getResourceAsStream("/person.csv")).get();


        final ReportService reportService = gd.getReportService();

        reportService.exportReport(definition, ReportExportFormat.CSV, new FileOutputStream("report.csv"));


        DataStoreService dataStoreService = gd.getDataStoreService();

        dataStoreService.upload("/dir/file.txt", new FileInputStream("file.txt"));
        InputStream stream = dataStoreService.download("/dir/file.txt");
        dataStoreService.delete("/dir/file.txt");

        gd.logout();
        */
        GoodData gd = new GoodData("obe-devel4.na.intgdc.com","bear@gooddata.com","jindrisska");
//        ProjectService projectService = gd.getProjectService();
//        DatasetService datasetService = gd.getDatasetService();
//        Project project = projectService.getProjectById("iq3ai5qmm6wnqmwkx9azp23t8x99wkgn");
//        DatasetManifest datasetManifest = datasetService.getDatasetManifest(project,"dataset.job");
//        DatasetManifest datasetManifest1 = datasetService.getDatasetManifest(project,"dataset.person");
//        datasetManifest1.setSource(Example.class.getResourceAsStream("/example1.csv"));
//        datasetManifest.setSource(Example.class.getResourceAsStream("/example.csv"));
//        gd.getDatasetService().loadDatasets(project,datasetManifest,datasetManifest1).get();
//        System.out.println("a");
        LoginSSTRetrievalStrategy loginSSTRetrievalStrategy = new LoginSSTRetrievalStrategy("bear@gooddata.com","jindrisska");
        HttpHost httpHost = new HttpHost("obe-devel4.na.intgdc.com",443,"https");
        GoodDataHttpClient goodDataHttpClient = new GoodDataHttpClient(httpHost,loginSSTRetrievalStrategy);
        String sst = loginSSTRetrievalStrategy.obtainSst(HttpClientBuilder.create().build(),httpHost);
        SimpleSSTRetrievalStrategy simpleSSTRetrievalStrategy = new SimpleSSTRetrievalStrategy(sst);
        simpleSSTRetrievalStrategy.
    }

}
