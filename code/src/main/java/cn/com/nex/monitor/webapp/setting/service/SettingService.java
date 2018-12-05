package cn.com.nex.monitor.webapp.setting.service;

import cn.com.nex.monitor.webapp.common.MonitorPasswordEncoder;
import cn.com.nex.monitor.webapp.setting.bean.ThresholdBean;
import cn.com.nex.monitor.webapp.setting.dao.SettingDao;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingService {
    @Autowired
    private SettingDao settingDao;

    @Autowired
    MonitorPasswordEncoder passwordEncoder;

    public boolean checkOldPassword(String userId, String oldPassword) {
        String old = settingDao.getOldPassword(userId);
        return oldPassword.equals(old) || passwordEncoder.encode(oldPassword).equals(old);
    }

    @Transactional
    public String changePassword(String userId, String newPassword) {
        String encryptPassword = passwordEncoder.encode(newPassword);
        settingDao.changePassword(userId, encryptPassword);

        return encryptPassword;
    }

    @Transactional
    public int updateProfile(UserBean user) {
        return settingDao.updateProfile(user);
    }

    public ThresholdBean getThreshold(String type) {
        return settingDao.getThreshold(type);
    }

    @Transactional
    public void updateThreshold(ThresholdBean threshold) {
        settingDao.updateThreshold(threshold);
    }
}
