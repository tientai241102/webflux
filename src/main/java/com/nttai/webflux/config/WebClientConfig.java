package com.nttai.webflux.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;
import reactor.util.retry.Retry;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Log4j2
public class WebClientConfig {


    @Bean(name = "thirdPartyEventLoop")
    public LoopResources thirdPartyEventLoop() {
        return LoopResources.create("third-party-event-loop", 8, true);
    }

    @Bean
    @ConfigurationProperties(prefix = "third-party.apis")
    public Map<String, ApiConfig> thirdPartyApiConfigs() {
        return new HashMap<>();
    }

    @Bean(name = "thirdPartyWebClients")
    public Map<String, WebClient> thirdPartyWebClients(@Qualifier("thirdPartyEventLoop") LoopResources loopResources) throws SSLException {
        Map<String, ApiConfig> apiConfigs = thirdPartyApiConfigs();
        Map<String, WebClient> webClients = new HashMap<>();

        for (Map.Entry<String, ApiConfig> entry : apiConfigs.entrySet()) {


            String apiName = entry.getKey();
            ApiConfig config = entry.getValue();

            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getTimeout())
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                    .runOn(loopResources)
                    .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(config.getTimeout() / 1000))
                            .addHandlerLast(new WriteTimeoutHandler(config.getTimeout() / 1000)));

            WebClient webClient = WebClient.builder()
                    .baseUrl(config.getUrl())
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .filter((request, next) ->
                            next.exchange(request)
                                    .timeout(Duration.ofSeconds(config.getTimeout() / 1000))
                                    .retryWhen(Retry.backoff(config.getRetryMaxAttempts(), Duration.ofMillis(config.getRetryDelayMs())))
                    )
                    .build();
            webClients.put(apiName, webClient);
        }
        return webClients;
    }


}