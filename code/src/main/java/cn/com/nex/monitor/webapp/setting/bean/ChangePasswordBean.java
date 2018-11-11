package cn.com.nex.monitor.webapp.setting.bean;

import cn.com.nex.monitor.webapp.common.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChangePasswordBean extends CommonBean {
    private String oldPassword;
    private String newPassword;
}
