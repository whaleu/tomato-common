package me.alphar.core.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class TmUserDetail extends User {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 用户id
     */
    @Getter
    private long userId;
    /**
     * 用户姓名
     */
    @Getter
    private String name;
    /**
     * 手机号
     */
    @Getter
    private String mobile;

    public TmUserDetail(long userId, String name, String mobile, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
    }
}
