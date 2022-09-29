package de.ohnes.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.ohnes.util.TestResult;

/**
 * A simple Client to push data to Elasticsearch
 * @implNote The usage of RestHighLevelClient is deprecated, warnings will be depressed, since it is not a concern here.
 */
@SuppressWarnings("deprecation")
public class MyElasticsearchClient {

    private static final Logger LOGGER = LogManager.getLogger(MyElasticsearchClient.class);

    private static RestHighLevelClient restHighLevelClient;
    
    private static List<TestResult> data = new ArrayList<TestResult>();


    // public ElasticsearchClient(String host, int port) {
    //     // Create the transport with a Jackson mapper
    //     ElasticsearchClient EsClient = new ElasticsearchClient() {
            
    //     };
    // }

    public static synchronized RestHighLevelClient makeConnection(String host) {
        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                new HttpHost(host, 9200)
            ));
        }
        return restHighLevelClient;
    }

    public static boolean pushData(String index) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        BulkRequest br = new BulkRequest();
        for(TestResult tr : data) {
            try {
                br.add(new IndexRequest(index).source(objectMapper.readValue(objectMapper.writeValueAsString(tr), typeRef)));
            } catch (IOException e) {
                LOGGER.warn("Couldn't reach ES Server. Saving data locally until next try.");
                return false;
            }
        }
        LOGGER.info("Trying to push {} Elements to Elasticsearch...", data.size());
        // System.out.println("Pushing " + data.size() + " Elements to Elasticsearch...");
        
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(br, RequestOptions.DEFAULT);
        } catch (Exception e) {
            LOGGER.warn("Couldn't reach ES Server. Saving data locally until next try.");
            return false;
        }
        data.clear();
        return true;
    }

    public static void addData(TestResult result) {
        MyElasticsearchClient.data.add(result);
    }
    
}
