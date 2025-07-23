package com.nttai.webflux.config.third_party;


import com.nttai.webflux.config.config.ApiConfig;
import com.nttai.webflux.config.constant.URLBaseTypeEnum;
import com.nttai.webflux.config.constant.URLEndpointTypeEnum;
import com.nttai.webflux.config.third_party.dto.PostResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class ThirdPartyService extends BaseApiService {


    public Mono<PostResponse[]> fetchDataGetPost() {
        String apiName = URLBaseTypeEnum.URL_BASE_GET_POST.getValue();
        String endpoint = URLEndpointTypeEnum.URL_ENDPOINT_GET_POST.getValue();
        WebClient webClient = thirdPartyWebClients.get(apiName);
        ApiConfig apiConfig = thirdPartyApiConfigs.get(apiName);
        if (webClient == null || apiConfig == null) {
            return Mono.error(new IllegalArgumentException("Invalid API name: " + apiName));
        }
        return callApi(apiConfig.getUrl(), endpoint, webClient, HttpMethod.GET, null, null, PostResponse[].class);
    }

    public Mono<PostResponse> fetchDataGetDetailPost(Integer postId) {
        String apiName = URLBaseTypeEnum.URL_BASE_GET_POST_DETAIL.getValue();
        String endpoint = String.format(URLEndpointTypeEnum.URL_ENDPOINT_GET_POST_DETAIL.getValue(), postId);
        WebClient webClient = thirdPartyWebClients.get(apiName);
        ApiConfig apiConfig = thirdPartyApiConfigs.get(apiName);
        if (webClient == null || apiConfig == null) {
            return Mono.error(new IllegalArgumentException("Invalid API name: " + apiName));
        }
        return callApi(apiConfig.getUrl(), endpoint, webClient, HttpMethod.GET, null, null, PostResponse.class);
    }
}