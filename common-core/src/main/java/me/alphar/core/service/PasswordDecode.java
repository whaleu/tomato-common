package me.alphar.core.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.SneakyThrows;

public class PasswordDecode {
    private static final String NOOP = "{noop}";

    /**
     * 密码解密
     * @param loginName 登录名
     * @param password 加密的密码
     * @return 解密后的密码
     */
    @SneakyThrows
    public static String decryptPassword(String loginName, String password) {
        if (password.startsWith(NOOP)) {
            return password.substring(6);
        }
        String md5Key = SecureUtil.md5(loginName).substring(8, 24);
        byte[] key = md5Key.getBytes(CharsetUtil.UTF_8);
        byte[] iv = md5Key.getBytes(CharsetUtil.UTF_8);
        AES aes = new AES(Mode.CBC, Padding.NoPadding, key, iv);
        byte[] res = aes.decrypt(Base64.decode(password.getBytes(CharsetUtil.UTF_8)));
        return new String(res, CharsetUtil.UTF_8).trim();
    }
}
