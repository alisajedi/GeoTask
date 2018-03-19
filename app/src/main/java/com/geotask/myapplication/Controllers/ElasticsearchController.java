package com.geotask.myapplication.Controllers;

import android.os.StrictMode;

import com.geotask.myapplication.DataClasses.Bid;
import com.geotask.myapplication.DataClasses.GTData;
import com.geotask.myapplication.DataClasses.Task;
import com.geotask.myapplication.DataClasses.User;
import com.geotask.myapplication.QueryBuilder.SuperBooleanBuilder;
import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.params.Parameters;

/*
API DOCUMENTATION
https://github.com/searchbox-io/Jest/tree/master/jest
 */


/**
 * controls all server networking code. Do not call explicitly. Use MasterController
 *
 */
public class ElasticsearchController {
    private static final String SERVER_ADDRESS = "http://cmput301.softwareprocess.es:8080";
    private static JestDroidClient client;
    private static String INDEX_NAME = "cmput301w18t23kyle";

    /**
     * Creates the index
     * @throws IOException
     */
    protected void createIndex() throws IOException {
        client.execute(new CreateIndex.Builder(INDEX_NAME).build());
    }

    /**
     * Delete the index
     * @throws IOException
     */
    protected int deleteIndex() throws IOException {
        JestResult result = client.execute(new DeleteIndex.Builder(INDEX_NAME).build());
        return result.getResponseCode();
    }

    /**
     * createNewDocument - creates the new document of whichever type it is passed
     * Note that the getType() function is overridden by every class that extends GTData. So in
     * other words if you pass it a Profile, it will be added to profile, if you pass it a Bid, it
     * will be added to bid ...
     *
     * @param data - GTData that should be added to the database
     * @return - ID of the document
     */
    protected void createNewDocument(GTData data) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        Index request = new Index.Builder(json)
                .index(INDEX_NAME)
                .type(data.getClass().toString())
                .id(data.getObjectID())
                .build();

        client.execute(request);
    }

    /**
     * getDocument - Get a single document by ID. Will downcast to correct object type.
     *
     * @param ID - ID of the document
     * @return GTData object
     */
    protected GTData getDocument(String ID, Type type) throws Exception {
        Get request = new Get.Builder(INDEX_NAME, ID).build();

        JestResult result = client.execute(request);

        GTData data = null;
        if (type.equals(Bid.class)) {
            data = result.getSourceAsObject(Bid.class);
        } else if (type.equals(Task.class)) {
            data = result.getSourceAsObject(Task.class);
        } else if (type.equals(User.class)) {
            data = result.getSourceAsObject(User.class);
        }
        return data;
    }

    /**
     * deleteDocument - deletes a document with the provided id of provided type
     *
     * @param ID - ID of document to be deleted
     * @param type - type of document
     * @return - response code of deletion (200 on success, 400 on failure)
     */
    protected int deleteDocument(String ID, Type type) throws IOException {
        return client.execute(new Delete.Builder(ID)
                        .index(INDEX_NAME)
                        .type(type.toString())
                        .build())
                .getResponseCode();
    }

    /**
     * scripting not turned on on server.
     * not atomic, use responsibly
     * @param data
     * @throws IOException
     */
    protected void updateDocument(GTData data) throws IOException {
        deleteDocument(data.getObjectID(), data.getClass());
        createNewDocument(data);
    }

    /**
     * searchBids - Method that searches the elasticSearch database and returns a list of bid objects
     *
     * @param query - query of terms that has been already formatted
     * @return a list of Bid objects
     */
    protected List<? extends GTData> search(String query, Type type) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Search search = new Search.Builder(query)
                .addIndex(INDEX_NAME)
                .addType(type.toString())
                .setParameter(Parameters.SIZE, 10000)
                .build();

        SearchResult result = client.execute(search);

        List<? extends GTData> dataList = null;
        if (type.equals(Bid.class)) {
            dataList = result.getSourceAsObjectList(Bid.class);
        } else if (type.equals(Task.class)) {
            dataList = result.getSourceAsObjectList(Task.class);
        } else if (type.equals(User.class)) {
            dataList = result.getSourceAsObjectList(User.class);
        }
        return dataList;
    }

    /**
     * existsProfile - Method for checking if an email is in use by another user
     *
     * @param email - email of the registering use
     * @return true if the email is in use, false otherwise
     */
    public boolean existsProfile(String email) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SuperBooleanBuilder query = new SuperBooleanBuilder();
        query.put("email", email);
        Search search = new Search.Builder(query.toString())
                .addIndex(INDEX_NAME)
                .addType(User.class.toString())
                .build();
        try {
            if(client.execute(search).getTotal() != 0){
                    return true;
                }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * verifySettings - Initializes client if not previously initialized
     */
    protected void verifySettings() {
        if(client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(SERVER_ADDRESS);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);

            client = (JestDroidClient) factory.getObject();
        }
    }

    /**
     * Turn off the light when you are done, this app is environmentally friendly
     */
    protected void shutDown() {
        if(client != null) {
            client.shutdownClient();
        }
    }

    /**
     * point server address to test index
     * @param testServerAddress
     */
    protected void setTestSettings(String testServerAddress) {
        INDEX_NAME = testServerAddress;
    }

}
