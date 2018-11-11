package cn.com.nex.monitor.webapp.common;

import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.MessageDigest;

public final class MonitorUtil {
    /**
     * 散列算法
     */
    private static final String ALGORITHM = "SHA-256";

    private MonitorUtil() {
    }

    /**
     * 密码散列。
     * @param password 密码
     * @return 散列后的密码
     */
    public static String hashEncode(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(password.getBytes());

            return bytesToHexString(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从spring-security上下文中获取当前用户信息。
     * @return 当前用户信息
     */
    public static UserBean getUserFromSecurity() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        UserBean user = (UserBean) auth.getPrincipal();

        return user;
    }

    /**
     * byte数组转换为字符串
     * @param bytes byte数组
     * @return 字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
