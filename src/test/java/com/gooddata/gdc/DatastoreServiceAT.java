package com.gooddata.gdc;

import com.gooddata.AbstractGoodDataAT;
import com.gooddata.GoodDataRestException;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Data store acceptance test
 */
public class DatastoreServiceAT extends AbstractGoodDataAT {

    private String file;
    private String directory;
    private static final int ITER_MAX = 10;

    @Test(groups = "datastore", dependsOnGroups = "account")
    public void datastoreUpload() throws Exception {
        DataStoreService dataStoreService = gd.getDataStoreService();

        directory = "/" + UUID.randomUUID().toString();
        file = directory + "/file.csv";
        dataStoreService.upload(file, getClass().getResourceAsStream("/person.csv"));
    }

    @Test(groups = "datastore", dependsOnMethods = "datastoreUpload")
    public void datastoreDownload() throws Exception {
        DataStoreService dataStoreService = gd.getDataStoreService();

        final File file = File.createTempFile("file", ".txt");
        try (InputStream stream = dataStoreService.download(this.file)) {
            file.deleteOnExit();
            IOUtils.copy(stream, new FileOutputStream(file));
        } finally {
            file.delete();
        }
    }

    @Test(groups = "datastore", dependsOnMethods = "datastoreDownload")
    public void datastoreDelete() throws Exception {
        DataStoreService dataStoreService = gd.getDataStoreService();
        dataStoreService.delete(this.file);
        dataStoreService.delete(this.directory);

        try {
            dataStoreService.delete(this.directory);
            fail("Exception was expected, as there is nothing to delete");
        } catch (GoodDataRestException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    @Test(groups = "datastore", dependsOnGroups = "account")
    public void datastoreConnectionsClosedAfterMultipleConnections() {
        DataStoreService dataStoreService = gd.getDataStoreService();
        directory = "/" + UUID.randomUUID().toString();
        for (int i = 0; i < ITER_MAX; i++) {
            dataStoreService.upload(directory + "/file" + i + ".csv", getClass().getResourceAsStream("/person.csv"));
        }
        assertThat(connManager.getTotalStats().getLeased(), is(equalTo(0)));
    }

    @Test(groups = "datastore", dependsOnGroups = "account")
    public void datastoreConnectionClosedAfterSingleConnection() throws Exception {
        DataStoreService dataStoreService = gd.getDataStoreService();

        directory = "/" + UUID.randomUUID().toString();
        file = directory + "/file.csv";
        dataStoreService.upload(file, getClass().getResourceAsStream("/person.csv"));
        assertThat(connManager.getTotalStats().getLeased(), is(equalTo(0)));
    }

    @Test(groups = "datastore", dependsOnGroups = "account")
    public void datastoreConnectionClosedMultipleThreads() throws  Exception{
        DataStoreService dataStoreService = gd.getDataStoreService();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        directory = "/" + UUID.randomUUID().toString();
        file = directory + "/file.csv";
        dataStoreService.upload(file, getClass().getResourceAsStream("/person.csv"));
        List<Future> list = new ArrayList<>();
        for(int i = 0; i < ITER_MAX; i++){
            list.add(executorService.submit(new MyRunnable(directory+"/file"+i+".csv",dataStoreService)));
        }
        int inc = 0;
        for(Future future : list){
            future.get();
        }
        System.out.println(inc);
        assertThat(connManager.getTotalStats().getLeased(), is(equalTo(0)));

    }

    static class MyRunnable implements Runnable{
        String file;
        DataStoreService dataStoreService;

        MyRunnable(String file, DataStoreService dataStoreService){
            this.file = file;
            this.dataStoreService = dataStoreService;
        }

        @Override
        public void run() {
            dataStoreService.upload(file,getClass().getResourceAsStream("/person.csv"));
        }
    }
}
