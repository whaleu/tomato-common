package me.alphar.core.annotation;

import me.alphar.core.config.ResourceServerConfig;
import me.alphar.core.config.TmResourceServerConfigurerAdapter;
import me.alphar.core.config.PermitAllUrlProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ResourceServerConfig.class, PermitAllUrlProperties.class, TmResourceServerConfigurerAdapter.class})
public @interface EnableTmResourceServer {
}
