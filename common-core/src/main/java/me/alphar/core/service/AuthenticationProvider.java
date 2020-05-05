package me.alphar.core.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.alphar.core.utils.CommonUtil;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Getter
    @Setter
    private TmUserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        if (userDetails == null) {
            throw new AccountExpiredException("用户不存在");
        }

        TmUserDetail laUserDetails = (TmUserDetail) userDetails;
        if (laUserDetails.getUserId() == 1) {
            throw new AccountExpiredException("系统管理员不能直接登录");
        }
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();
        password = PasswordDecode.decryptPassword(laUserDetails.getUsername(), password);
        String md5Password = CommonUtil.getPasswordMD5(laUserDetails.getUserId(), password);
        if (!md5Password.equals(userDetails.getPassword())) {
            throw new AccountExpiredException("用户名密码有误");
        }
    }

    @Override
    protected UserDetails retrieveUser(String loginName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();
        password = PasswordDecode.decryptPassword(loginName, password);
        UserDetails userDetails;

        try {
            // 1. 获取用户
            userDetails = userDetailsService.loadUserByUsername(loginName);
            // 2. 本地密码和输入密码对比
            additionalAuthenticationChecks(userDetails, usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new AuthenticationServiceException("登录出错");
        }
        return userDetails;
    }
}
