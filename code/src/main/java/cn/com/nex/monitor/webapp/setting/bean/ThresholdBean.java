package cn.com.nex.monitor.webapp.setting.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ThresholdBean extends CommonBean {
    private String dataId;
    private int norml;
    private int warn;
}
