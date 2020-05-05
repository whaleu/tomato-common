package me.alphar.core.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import javax.annotation.Resource;

@Configuration
public class TmOauth2FeignConfiguration {

    @Resource
    private PermitAllUrlProperties permitAllUrlProperties;

    @Bean
    @ConditionalOnProperty("security.oauth2.client.client-id")
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                                            OAuth2ProtectedResourceDetails resource,
                                                            AccessTokenContextRelay accessTokenContextRelay) {
        return new TmOAuth2FeignRequestInterceptor(oAuth2ClientContext, resource, accessTokenContextRelay, permitAllUrlProperties);
    }

    @Bean
    @ConditionalOnProperty("security.oauth2.client.client-id")
    public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext context) {
        return new AccessTokenContextRelay(context);
    }
}
