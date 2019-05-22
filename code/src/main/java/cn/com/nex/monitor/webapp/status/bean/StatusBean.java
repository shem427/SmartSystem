package cn.com.nex.monitor.webapp.status.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StatusBean extends CommonBean {
    private String unitId;
    private String unitName;
    private String unitPath;
    private String unitIdChain;
    private int normalLight;
    private int warningLight;
    private int errorLight;
    private int activeSensor;
    private int inactiveSensor;
    private int normalRadiationData;
    private int warningRadiationData;
    private int errorRadiationData;
}
