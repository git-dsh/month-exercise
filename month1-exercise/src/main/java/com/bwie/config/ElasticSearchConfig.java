package com.bwie.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ElasticSearchConfig {
    @Bean
    public RestHighLevelClient initElasticSearch(){
        HttpHost localhost = new HttpHost("192.168.56.131",9200,"http");
        RestClientBuilder builder = RestClient.builder(localhost);
        return new RestHighLevelClient(builder);
    }
}
