package me.alphar.core.config;

import feign.RequestTemplate;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TmOAuth2FeignRequestInterceptor extends OAuth2FeignRequestInterceptor {

    private final OAuth2ClientContext oAuth2ClientContext;

    private final AccessTokenContextRelay accessTokenContextRelay;

    private PermitAllUrlProperties permitAllUrlProperties;

    private final AntPathMatcher antPathMatcher;

    public TmOAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                           OAuth2ProtectedResourceDetails resource,
                                           AccessTokenContextRelay accessTokenContextRelay,
                                           PermitAllUrlProperties permitAllUrlProperties) {
        super(oAuth2ClientContext, resource);
        this.accessTokenContextRelay = accessTokenContextRelay;
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.permitAllUrlProperties = permitAllUrlProperties;
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    public void apply(RequestTemplate template) {

        if (template.method().equals("GET")) {
            resetTemplate(template);
        }

        //访问资源服务中直接暴露的端点不拷贝token
        if (isPermitAllUrl(template.url())) {
            return;
        }

        try {
            accessTokenContextRelay.copyToken();
            if (oAuth2ClientContext != null
                    && oAuth2ClientContext.getAccessToken() != null) {
                super.apply(template);
            }
        } catch (Exception ex) {
            //访问资源服务中直接暴露的端点不拷贝token
            if (!isPermitAllUrl(template.url())) {
                throw ex;
            }
        }
    }

    /**
     * 将以@RequestParam传递过来的GET POJO对象拆分为URL参数传输
     * SearchPageDTO(nowPage=1, onePageCount=10, dataCount=0, pageCount=0, startIndex=0, orderBy=null)
     * --> nowPage=1&onePageCount=10&dataCount=0&pageCount=0&startIndex=0
     */
    private void resetTemplate(RequestTemplate template) {

        if (null == template.queries() || template.queries().size() == 0) {
            return;
        }
        Map<String, Collection<String>> queries = new HashMap<>();
        for (Map.Entry<String, Collection<String>> kv : template.queries().entrySet()) {
            String valueStr = kv.getValue().toArray()[0].toString();
            if (valueStr.contains("=") && valueStr.contains("(") && valueStr.endsWith(")")) {
                String objStr = valueStr.substring(valueStr.indexOf('(') + 1);
                objStr = objStr.substring(0, objStr.length() - 1);
                String[] items = objStr.split("=|,");
                for (int i = 0; i < items.length; i++) {
                    if (i % 2 != 0) {
                        continue;
                    }
                    if (i + 1 >= items.length) {
                        break;
                    }
                    if ("null".equals(items[i + 1].trim())) {
                        continue;
                    }
                    Collection<String> values = new ArrayList<>();
                    values.add(items[i + 1].trim());
                    queries.put(items[i].trim(), values);
                }
            } else {
                Collection<String> values = new ArrayList<>();
                values.add(valueStr);
                queries.put(kv.getKey(), values);
            }
        }
        template.queries(null);
        template.queries(queries);
    }

    /**
     * 匹配暴露端点
     *
     * @param requestUrl
     * @return
     * @author huiyu
     * @date 2019/10/21
     */
    private boolean isPermitAllUrl(String requestUrl) {
        if (null == permitAllUrlProperties || null == permitAllUrlProperties.getPermitAllUrls()
                || permitAllUrlProperties.getPermitAllUrls().isEmpty()) {
            return false;
        }
        for (String pattern : permitAllUrlProperties.getPermitAllUrls()) {
            if (antPathMatcher.match(pattern, requestUrl)) {
                return true;
            }
        }
        return false;
    }
}