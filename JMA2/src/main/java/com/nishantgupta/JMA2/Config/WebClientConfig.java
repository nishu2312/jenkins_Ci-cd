package com.nishantgupta.JMA2.Config;

import io.netty.channel.ChannelOption;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;



@Configuration
public class WebClientConfig {

    private static final String RANDOM_USER_API_URL = "https://randomuser.me/api/";
    private static final String NATIONALIZE_API_URL = "https://api.nationalize.io/";
    private static final String GENDERIZE_API_URL = "https://api.genderize.io/";

    @Bean
    WebClient api1WebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(2000))
                                .addHandlerLast(new WriteTimeoutHandler(2000)))))
                .baseUrl(RANDOM_USER_API_URL)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean
    WebClient api2WebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000))
                                .addHandlerLast(new WriteTimeoutHandler(1000)))))
                .baseUrl(NATIONALIZE_API_URL)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean
    WebClient api3WebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000))
                                .addHandlerLast(new WriteTimeoutHandler(1000)))))
                .baseUrl(GENDERIZE_API_URL)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
}
