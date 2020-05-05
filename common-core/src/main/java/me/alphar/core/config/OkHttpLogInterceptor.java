package me.alphar.core.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
public class OkHttpLogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        log.info("okhttp method:{}",chain.request().method());
        log.info("okhttp request:{}",chain.request().body());
        return chain.proceed(chain.request());
    }
}
