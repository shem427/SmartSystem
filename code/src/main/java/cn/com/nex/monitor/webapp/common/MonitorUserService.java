package cn.com.nex.monitor.webapp.common;

import cn.com.nex.monitor.webapp.common.constant.MonitorConstant;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.user.dao.UserDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员登陆Service类
 */
@Service
public class MonitorUserService implements UserDetailsService {

    @Autowired
    protected UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserBean user = userDao.get(userId);
        if (user == null) {
            throw new UsernameNotFoundException(userId + " not exist!");
        } else if (!user.isActive()) {
            throw new UsernameNotFoundException(userId + " not active!");
        }

        user.setAuthorities(getAuthorities(user.getUserRoles()));
        return user;
    }

    /**
     * 根据用户Role设置权限。
     * @param userRoles db中存储的role
     * @return 用户权限
     */
    private List<GrantedAuthority> getAuthorities(String userRoles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (StringUtils.isEmpty(userRoles)) {
            return grantedAuthorities;
        }
        String[] roles = userRoles.split(MonitorConstant.ROLE_SEPERATOR);
        for (String role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return grantedAuthorities;
    }
}
