package de.ohnes.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.ohnes.util.TestResult;

public class MyElasticsearchClient {

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
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("Pushing " + data.size() + " Elements to Elasticsearch...");
        data.clear();

        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(br, RequestOptions.DEFAULT);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void addData(TestResult result) {
        MyElasticsearchClient.data.add(result);
    }
    
}
