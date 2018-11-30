package cn.com.nex.monitor.webapp.setting.dao;

import cn.com.nex.monitor.webapp.common.DBConstant;
import cn.com.nex.monitor.webapp.common.MonitorPasswordEncoder;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SettingDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int changePassword(String userId, String cryptedPassword) {
        String sql = "UPDATE `USER` SET `USER_PASSWORD`=? where `USER_ID`=?";
        return jdbcTemplate.update(sql, cryptedPassword, userId);
    }

    public String getOldPassword(String userId) {
        String sql = "SELECT `USER_PASSWORD` FROM `USER` WHERE `USER_ID`=?";
        String password = jdbcTemplate.query(sql, new String[] { userId }, rs -> {
            String pwd = null;
            while(rs.next()) {
                pwd = rs.getString(DBConstant.PASSWORD);
            }
            return pwd;
        });

        return password;
    }

    public int updateProfile(UserBean profile) {
        String sql = "UPDATE `USER` SET `POLICE_NUMBER`=?,`USER_NAME`=?,`PHONE_NUMBER`=? WHERE `USER_ID`=?";
//        return jdbcTemplate.update(sql,
//                profile.getPoliceNumber(),
//                profile.getName(),
//                profile.getPhoneNumber(),
//                profile.getUserId());
        return 0;
    }
}
