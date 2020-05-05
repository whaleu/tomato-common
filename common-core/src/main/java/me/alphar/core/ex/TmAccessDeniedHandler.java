package me.alphar.core.ex;

import me.alphar.core.Res;
import me.alphar.core.ex.serializer.TmResourceOauthExceptionSerializer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TmAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        TmResourceOauthExceptionSerializer.writerError(Res.error(e.getMessage()), httpServletResponse);
    }
}
