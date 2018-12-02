package cn.com.nex.monitor.webapp.user.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserBean extends CommonBean implements UserDetails {
    private String userId;
    private String name;
    private String mailAddress;
    private String userRoles;
    private String phoneNumber;

    private List<? extends GrantedAuthority> authorities;
    @JsonIgnore
    private String password;

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
