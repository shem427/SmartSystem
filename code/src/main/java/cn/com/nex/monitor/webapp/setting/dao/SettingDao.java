package cn.com.nex.monitor.webapp.setting.dao;

import cn.com.nex.monitor.webapp.common.constant.DBConstant;
import cn.com.nex.monitor.webapp.setting.bean.ThresholdBean;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        String sql = "UPDATE `USER` SET `USER_NAME`=?,`MAIL_ADDRESS`=?,`PHONE_NUMBER`=? WHERE `USER_ID`=?";
        return jdbcTemplate.update(sql,
                profile.getName(),
                profile.getMailAddress(),
                profile.getPhoneNumber(),
                profile.getUserId());
    }

    public ThresholdBean getThreshold(String type) {
        String sql = "SELECT `TYPE`, `NORMAL`, `WARNING` FROM `THRESHOLD` WHERE TYPE=?";
        return jdbcTemplate.query(sql, new String[] {type}, rs -> {
            if (rs.next()) {
                ThresholdBean bean = new ThresholdBean();
                bean.setNormal(rs.getInt("NORMAL"));
                bean.setWarning(rs.getInt("WARNING"));
                return bean;
            } else {
                return null;
            }
        });
    }

    public void updateThreshold(ThresholdBean threshold) {
        String sql = "UPDATE `THRESHOLD` SET `NORMAL`=?,`WARNING`=? WHERE `TYPE`=?";
        int count = jdbcTemplate.update(sql, threshold.getNormal(), threshold.getWarning(), threshold.getType());

        if (count == 0) {
            sql = "INSERT INTO `THRESHOLD` VALUES (?,?,?)";
            jdbcTemplate.update(sql, threshold.getType(), threshold.getNormal(), threshold.getWarning());
        }
    }
}
