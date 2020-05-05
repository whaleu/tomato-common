package me.alphar.core.utils;

import cn.hutool.crypto.digest.DigestUtil;

public class CommonUtil {

    private CommonUtil() {}

    /**
     * 全角转半角
     *
     * @param source 源字符串
     * @return 半角字符串
     */
    public static String toDBC(String source) {
        char[] c = source.toCharArray();
        for (int index = 0; index < c.length; index++) {
            if (c[index] == 12288) {
                // 全角空格
                c[index] = (char) 32;

            } else if (c[index] > 65280 && c[index] < 65375) {
                // 其他全角字符
                c[index] = (char) (c[index] - 65248);
            }
        }
        return String.valueOf(c);

    }

    /**
     * 获取MD5化的密码
     *
     * @param tid tid
     * @param password 密码
     * @return MD5化的密码
     */
    public static String getPasswordMD5(long tid, String password) {
        password = password + String.valueOf(tid) + tid % 190121;
        password = password.toUpperCase();
        password = toDBC(password);
        return DigestUtil.md5Hex(password).toUpperCase();
    }
}
