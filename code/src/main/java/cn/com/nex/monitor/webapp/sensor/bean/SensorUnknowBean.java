package cn.com.nex.monitor.webapp.sensor.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SensorUnknowBean {
    private String radiationModelId;
    private int dataCount;
}
