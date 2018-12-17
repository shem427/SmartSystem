package cn.com.nex.monitor.webapp.warn.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UnitWarnBean extends CommonBean {
    private int warnId;
    private String unitId;
    private int unitStatus;
    private Date notifyTime;
}
