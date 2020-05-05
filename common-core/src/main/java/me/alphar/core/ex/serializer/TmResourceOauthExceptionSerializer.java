package me.alphar.core.ex.serializer;

import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.alphar.core.Res;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

public class TmResourceOauthExceptionSerializer {

    private TmResourceOauthExceptionSerializer() {}

    /**
     * 资源服务器统一处理的异常返回
     *
     */
    @SneakyThrows
    public static void writerError(Res r, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setStatus(HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(r));
    }
}
