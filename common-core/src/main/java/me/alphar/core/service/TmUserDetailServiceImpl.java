package me.alphar.core.service;

import me.alphar.core.Res;
import me.alphar.core.Wrapper;
import me.alphar.core.entity.InnerUser;
import me.alphar.core.ex.BusinessException;
import me.alphar.core.feign.FeignInnerUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TmUserDetailServiceImpl implements TmUserDetailsService {

//    @Resource
//    private IUserService userService;

    private final FeignInnerUserService innerUserService;

    public TmUserDetailServiceImpl(FeignInnerUserService innerUserService) {
        this.innerUserService = innerUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        // 1. 根据用户获取用户
//        me.alphar.auth.entity.User dbUser = userService.getUserByUsername(s);
        Res<InnerUser> res = innerUserService.getByLoginName(loginName);
        if (res == null || res.getCode() == Wrapper.ERROR_CODE) {
            throw new BusinessException("获取用户失败");
        }
        InnerUser innerUser = res.getData();
        if (innerUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 2. 将用户数据封装到 UserDetail 中，由 SpringSecurity 判断
//        User.UserBuilder user = User.withUsername(s);
        return new User(innerUser.getLoginName(), "{noop}" + innerUser.getPassword(), new ArrayList<>());
    }
}
