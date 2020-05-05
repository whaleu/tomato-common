package me.alphar.core.feign;

import me.alphar.core.Res;
import me.alphar.core.entity.InnerUser;
import me.alphar.core.feign.factory.FeignInnerUserServiceFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "userserver", fallbackFactory = FeignInnerUserServiceFallBackFactory.class)
public interface FeignInnerUserService {

    @GetMapping("innerUser/api/login/getByLoginName/{loginName}")
    Res<InnerUser> getByLoginName(@PathVariable("loginName") String loginName);
}
