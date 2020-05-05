package me.alphar.core.ex;

import me.alphar.core.Res;
import me.alphar.core.ex.serializer.TmResourceOauthExceptionSerializer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Authentication异常的统一处理
 */
@Component
public class TmResourceExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) {
        TmResourceOauthExceptionSerializer.writerError(Res.error(e.getMessage()), response);
    }
}
