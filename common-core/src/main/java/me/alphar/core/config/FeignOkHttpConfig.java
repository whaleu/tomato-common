package me.alphar.core.config;

import feign.Client;
import feign.Contract;
import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass({Feign.class})
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean({Client.class})
//    public Client feignClient(okhttp3.OkHttpClient client) {
//        return new feign.okhttp.OkHttpClient(client);
//    }
//
    @Bean
    @ConditionalOnMissingBean({ConnectionPool.class})
    public ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties, OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        Integer maxTotalConnections = httpClientProperties.getMaxConnections();
        Long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory,
                               ConnectionPool connectionPool,
                               FeignHttpClientProperties httpClientProperties) {
        Boolean followRedirects = httpClientProperties.isFollowRedirects();
        Integer connectTimeout = httpClientProperties.getConnectionTimeout();
        Boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
        return httpClientFactory.createBuilder(disableSslValidation)
                .connectTimeout((long)connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .addInterceptor(new OkHttpLogInterceptor()) // 自定义请求日志拦截器
                .build();
    }

//    @Bean
//    public okhttp3.OkHttpClient okHttpClient() {
//        return new okhttp3.OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS)
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool())
//                // .addInterceptor();
//                .build();
//    }
}
