package me.alphar.core.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.alphar.core.Res;
import me.alphar.core.entity.InnerUser;
import me.alphar.core.feign.FeignInnerUserService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignInnerUserServiceFallBackImpl implements FeignInnerUserService {

    @Setter
    private Throwable cause;

    @Override
    public Res<InnerUser> getByLoginName(String loginName) {
        log.error("feign 查询登录用户信息失败", cause);
        return Res.error(cause.getMessage());
    }
}
