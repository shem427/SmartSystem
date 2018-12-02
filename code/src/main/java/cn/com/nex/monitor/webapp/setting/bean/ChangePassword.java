package cn.com.nex.monitor.webapp.setting.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChangePassword extends CommonBean {
    private String oldPassword;
    private String newPassword;
}
