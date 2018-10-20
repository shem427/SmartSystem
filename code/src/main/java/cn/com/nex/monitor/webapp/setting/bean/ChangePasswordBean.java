package cn.com.nex.monitor.webapp.setting.bean;

import cn.com.nex.monitor.webapp.common.CommonBean;
import lombok.Data;

@Data
public class ChangePasswordBean extends CommonBean {
    private String oldPassword;
    private String newPassword;
}
