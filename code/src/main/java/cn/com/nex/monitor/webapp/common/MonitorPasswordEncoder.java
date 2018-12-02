package cn.com.nex.monitor.webapp.common;

import cn.com.nex.monitor.webapp.common.util.MonitorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密类
 */
@Component
public class MonitorPasswordEncoder implements PasswordEncoder {

    /**
     * 对密码进行加密。
     * @param charSequence 密码明文
     * @return 加密密码
     */
    @Override
    public String encode(CharSequence charSequence) {
        return MonitorUtil.hashEncode(charSequence.toString());
    }

    /**
     * 判断明文密码与密文是否一致。
     * @param rawPassword 密码明文
     * @param encodedPassword 密码密文
     * @return 是否一致（布尔值）？
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword.toString())
                || encodedPassword.equals(MonitorUtil.hashEncode(rawPassword.toString()));
    }
}
