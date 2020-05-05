package me.alphar.core.feign.factory;

import feign.hystrix.FallbackFactory;
import me.alphar.core.feign.FeignInnerUserService;
import me.alphar.core.feign.fallback.FeignInnerUserServiceFallBackImpl;
import org.springframework.stereotype.Component;

@Component
public class FeignInnerUserServiceFallBackFactory
    implements FallbackFactory<FeignInnerUserService> {

    @Override
    public FeignInnerUserService create(Throwable throwable) {
        FeignInnerUserServiceFallBackImpl feignInnerUserServiceFallBack = new FeignInnerUserServiceFallBackImpl();
        feignInnerUserServiceFallBack.setCause(throwable);
        return null;
    }
}
