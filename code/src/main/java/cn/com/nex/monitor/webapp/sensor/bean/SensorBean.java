package cn.com.nex.monitor.webapp.sensor.bean;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SensorBean extends CommonBean {
    private String sensorId;
    private String sensorName;
    private String sensorSn;
    private String sensorModel;
    private String sensorRemark;
    private String unitId;
    private String unitName;
    private String unitFullPath;
}
